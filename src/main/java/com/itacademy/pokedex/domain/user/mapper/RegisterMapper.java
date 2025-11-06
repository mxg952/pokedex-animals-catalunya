package com.itacademy.pokedex.domain.user.mapper;

import com.itacademy.pokedex.domain.user.dto.RegisterRequest;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.security.dto.JwtResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.itacademy.pokedex.domain.user.modelo.Role.USER_ROLE;

@Slf4j
@Component
public class RegisterMapper {

    public User toEntity(RegisterRequest request, PasswordEncoder encoder) {
        return User.builder()
                .name(request.getName())
                .password(encoder.encode(request.getPassword()))
                .role(USER_ROLE)
                .build();
    }

    public JwtResponse toDto(User user, String token) {
        return new JwtResponse(token, user.getName());
    }
}
