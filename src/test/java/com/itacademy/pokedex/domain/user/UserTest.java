package com.itacademy.pokedex.domain.user;

import com.itacademy.pokedex.domain.user.dto.RegisterRequest;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @InjectMocks
    UserService userService;

    @Test
    void givenValidUser_whenRegister_thenUserIsSavedWithEncodedPassword() {
        RegisterRequest request = new RegisterRequest("marc", "1234");

        when(userRepository.existsByUsername("marc")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encoded1234");

        userService.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals("marc", savedUser.getName());
        assertEquals("encoded1234", savedUser.getPassword());
    }
}
