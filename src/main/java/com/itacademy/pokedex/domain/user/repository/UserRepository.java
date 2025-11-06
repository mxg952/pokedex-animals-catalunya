package com.itacademy.pokedex.domain.user.repository;

import com.itacademy.pokedex.domain.user.modelo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String name);
    Optional<User> findByName(String name);
}
