package com.qi.startup.service;

import com.qi.startup.dto.AuthResponse;
import com.qi.startup.dto.LoginRequest;
import com.qi.startup.dto.RegisterRequest;
import com.qi.startup.model.User;
import com.qi.startup.repository.UserRepository;
import com.qi.startup.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        // 安全修复：忽略客户端传入的 role，强制注册为 ENTREPRENEUR
        // 只有管理员才能提升用户角色
        user.setRole(User.UserRole.ENTREPRENEUR);
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setCity(request.getCity());

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());

        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name(),
            user.getRealName(),
            user.getPhone(),
            user.getCity()
        );

        return new AuthResponse(token, userDto);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());

        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name(),
            user.getRealName(),
            user.getPhone(),
            user.getCity()
        );

        return new AuthResponse(token, userDto);
    }

    public AuthResponse.UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        return new AuthResponse.UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name(),
            user.getRealName(),
            user.getPhone(),
            user.getCity()
        );
    }

    // 初始化管理员账号（仅在没有管理员时可用）
    public Map<String, Object> initAdmin() {
        // 检查是否已有管理员
        long adminCount = userRepository.countByRole(User.UserRole.ADMIN);
        if (adminCount > 0) {
            throw new RuntimeException("管理员账号已存在，无法重复初始化");
        }

        // 创建管理员账号
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@chuangqi.com");
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setRole(User.UserRole.ADMIN);
        admin.setRealName("系统管理员");

        userRepository.save(admin);

        // 生成 token
        String token = jwtUtil.generateToken(admin.getId(), admin.getUsername(), admin.getRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "管理员账号初始化成功");
        response.put("token", token);
        response.put("user", new AuthResponse.UserDto(
            admin.getId(),
            admin.getUsername(),
            admin.getEmail(),
            admin.getRole().name(),
            admin.getRealName(),
            admin.getPhone(),
            admin.getCity()
        ));

        return response;
    }
}
