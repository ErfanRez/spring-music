package com.music.dto;

import com.music.model.User;
import com.music.utils.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class ArtistDto {
    @NotBlank(message = "Firstname is required")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    private String lastName;

    @NotBlank(message = "Nickname is required")
    private String nickname;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public static User dtoToArtist(ArtistDto dto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setNickname(dto.getNickname());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(Roles.ARTIST);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return user;
    }
}
