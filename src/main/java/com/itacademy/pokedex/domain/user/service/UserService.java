package com.itacademy.pokedex.domain.user.service;

import com.itacademy.pokedex.domain.user.dto.LoginRequest;
import com.itacademy.pokedex.domain.user.dto.RegisterRequest;
import com.itacademy.pokedex.domain.user.exception.UserNotFoundException;
import com.itacademy.pokedex.domain.user.exception.UserNameAlreadyExistsException;
import com.itacademy.pokedex.domain.user.mapper.RegisterMapper;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.security.dto.JwtResponse;
import com.itacademy.pokedex.security.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;
    private RegisterMapper registerMapper;
    private PasswordEncoder encoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;


    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getName())) {
            throw new UserNameAlreadyExistsException(request.getName());
        }
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

        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new UserNotFoundException("No existeix cap usuari amb aquest nom..."));

        String token = jwtService.generateToken(user);

        return JwtResponse.builder()
                .token(token)
                .name(user.getName())
                .build();
    }

    public void logout(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            throw new IllegalArgumentException("Token no proporcionat");
        }

        String token = authHeader;

        // Netejar el prefix "Bearer " si existeix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Invalidar el token encara que sigui invàlid o expirat
        // Això evita que pugui ser reutilitzat
        try {
            // Verificar que el token té un format bàsicament correcte
            if (!token.isBlank()) {
                jwtService.invalidateToken(token);
                log.info("Token invalidat per logout");
            }
        } catch (Exception e) {
            log.warn("Error durant invalidació de token: {}", e.getMessage());
            // No llençar l'excepció per no revelar informació
        }
    }

}
