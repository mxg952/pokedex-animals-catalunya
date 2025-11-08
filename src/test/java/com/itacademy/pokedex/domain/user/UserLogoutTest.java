package com.itacademy.pokedex.domain.user;

import com.itacademy.pokedex.domain.user.mapper.RegisterMapper;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.domain.user.service.UserService;
import com.itacademy.pokedex.security.service.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserLogoutTest {

    @Mock
    UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RegisterMapper registerMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    UserService userService;

    @Test
    void givenValidToken_whenLogout_thenTokenIsInvalidated() {
        // Given
        String authHeader = "Bearer valid.jwt.token";
        String cleanToken = "valid.jwt.token";

        when(jwtService.extractUsername(cleanToken)).thenReturn("marc");

        // When
        userService.logout(authHeader);

        // Then
        verify(jwtService).invalidateToken(cleanToken);
    }

    @Test
    void givenTokenWithoutBearerPrefix_whenLogout_thenTokenIsInvalidated() {
        // Given
        String token = "raw.jwt.token";

        when(jwtService.extractUsername(token)).thenReturn("marc");

        // When
        userService.logout(token);

        // Then
        verify(jwtService).invalidateToken(token);
    }

    @Test
    void givenNullToken_whenLogout_thenThrowException() {
        // When & Then
        assertThatThrownBy(() -> userService.logout(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Token no proporcionat");
    }

    @Test
    void givenEmptyToken_whenLogout_thenThrowException() {
        // When & Then
        assertThatThrownBy(() -> userService.logout(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Token no proporcionat");
    }

    @Test
    void givenInvalidToken_whenLogout_thenDoNotThrow() {
        // Given
        String invalidToken = "Bearer invalid.token";

        when(jwtService.extractUsername("invalid.token"))
                .thenThrow(new RuntimeException("Token invàlid"));

        // When & Then - No hauria de llençar excepció
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            userService.logout(invalidToken);
        });

        // Verify que s'ha cridat invalidateToken malgrat l'error
        verify(jwtService).invalidateToken("invalid.token");
    }
}
