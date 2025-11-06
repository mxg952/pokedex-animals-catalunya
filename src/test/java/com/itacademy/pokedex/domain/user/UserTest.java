package com.itacademy.pokedex.domain.user;

import com.itacademy.pokedex.domain.user.dto.LoginRequest;
import com.itacademy.pokedex.domain.user.dto.RegisterRequest;
import com.itacademy.pokedex.domain.user.mapper.RegisterMapper;
import com.itacademy.pokedex.domain.user.modelo.Role;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.domain.user.service.UserService;
import com.itacademy.pokedex.security.dto.JwtResponse;
import com.itacademy.pokedex.security.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserTest {

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
    void givenValidUser_whenRegister_thenUserIsSavedWithEncodedPassword() {
        RegisterRequest request = new RegisterRequest("marc", "12348");

        User mappedUser = User.builder()
                .name("marc")
                .password("12348") // sense codificar, el service ho codificarà
                .build();

        when(registerMapper.toEntity(request, passwordEncoder)).thenReturn(mappedUser);

        userService.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals("marc", savedUser.getName());
        assertEquals("12348", savedUser.getPassword());
    }

    @Test
    void givenValidCredentials_whenLogin_thenReturnJwtResponse() {
        LoginRequest request = new LoginRequest("marc", "12345");

        User user = User.builder()
                .name("marc")
                .password("encoded12345")
                .role(Role.USER_ROLE)
                .build();

        String expectedToken = "jwt-token-123";

        when(userRepository.findByName("marc")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(expectedToken);

        JwtResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
        assertEquals("marc", response.getName());
    }
}
