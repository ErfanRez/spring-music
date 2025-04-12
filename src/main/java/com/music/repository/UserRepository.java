package com.music.repository;

import com.music.model.User;
import com.music.utils.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByUsernameOrEmailOrNickname(String username, String email, String nickname);

    List<User> findTop10ByRole(Roles role);

    List<User> findAllByRole(Roles role);
}
