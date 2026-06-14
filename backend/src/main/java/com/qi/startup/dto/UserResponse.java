package com.qi.startup.dto;

import com.qi.startup.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String realName;
    private String phone;
    private String city;
    private String company;
    private String position;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole() != null ? user.getRole().name() : null;
        this.realName = user.getRealName();
        this.phone = user.getPhone();
        this.city = user.getCity();
        this.company = user.getCompany();
        this.position = user.getPosition();
        this.createdAt = user.getCreatedAt();
    }
}
