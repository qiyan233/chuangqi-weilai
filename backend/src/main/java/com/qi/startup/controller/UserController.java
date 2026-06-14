package com.qi.startup.controller;

import com.qi.startup.dto.UserResponse;
import com.qi.startup.model.User;
import com.qi.startup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> listUsers(@RequestAttribute("userId") Long adminId) {
        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id,
            @RequestAttribute("userId") Long adminId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestAttribute("userId") Long adminId) {
        String roleStr = body.get("role");
        if (roleStr == null || roleStr.isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }

        User.UserRole newRole;
        try {
            newRole = User.UserRole.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid role: " + roleStr + ". Must be one of: ENTREPRENEUR, INVESTOR, ADMIN");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setRole(newRole);
        userRepository.save(user);

        return ResponseEntity.ok(new UserResponse(user));
    }
}
