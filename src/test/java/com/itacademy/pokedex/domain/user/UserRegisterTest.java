package com.itacademy.pokedex.domain.user;

import com.itacademy.pokedex.domain.user.dto.RegisterRequest;
import com.itacademy.pokedex.domain.user.exception.UserNameAlreadyExistsException;
import com.itacademy.pokedex.domain.user.mapper.RegisterMapper;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.domain.user.service.UserService;
import com.itacademy.pokedex.security.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRegisterTest {

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
    void givenExistingUsername_whenRegister_thenThrowException() {
        RegisterRequest request = new RegisterRequest("marc", "12345");

        when(userRepository.existsByName("marc")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(UserNameAlreadyExistsException.class)
                .hasMessageContaining("Username already exists");
    }

}
