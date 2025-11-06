package com.itacademy.pokedex.domain.user.service;

import com.itacademy.pokedex.domain.user.dto.LoginRequest;
import com.itacademy.pokedex.domain.user.dto.RegisterRequest;
import com.itacademy.pokedex.domain.user.exception.UserNotFoundException;
import com.itacademy.pokedex.domain.user.mapper.RegisterMapper;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.security.dto.JwtResponse;
import com.itacademy.pokedex.security.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private RegisterMapper registerMapper;
    private PasswordEncoder encoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;


    public JwtResponse register(RegisterRequest request) {
        User user = registerMapper.toEntity(request, encoder);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return JwtResponse.builder()
                .token(token)
                .name(user.getName())
                .build();
    }

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getName(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getName())
                .orElseThrow(() -> new UserNotFoundException("No existeix cap usuari amb aquest nom..."));

        String token = jwtService.generateToken(user);

        return JwtResponse.builder()
                .token(token)
                .name(user.getName())
                .build();
    }

}
