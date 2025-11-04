package com.itacademy.pokedex.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void givenValidUser_whenSaveUser_thenUserIsPersisted() {
        User user = new User();
        user.setId(1L);
        user.setName("Marc");
        user.setPassword("12345");

        Mockito.when(userService.save(any(User.class))).thenReturn(user);

        User result = userService.save(user);

        assertEquals(1L, result.getId());
        assertEquals("Marc", result.getName());
        assertEquals("12345", result.getPassword());
        Mockito.verify(userRepository).save(any(User.class));

    }
}
