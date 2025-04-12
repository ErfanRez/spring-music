package com.music.dto;

import com.music.model.User;
import com.music.utils.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegisterDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public RegisterDto() {
    }

    public static User dtoToUser(RegisterDto dto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return user;
    }
}
