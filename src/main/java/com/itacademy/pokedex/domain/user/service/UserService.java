package com.itacademy.pokedex.domain.user.service;

import com.itacademy.pokedex.domain.user.dto.RegisterRequest;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public void register(RegisterRequest request) {

        User user =

        userRepository.save(user);
    }

    public User getById(Long userId) {
        return userRepository.getReferenceById(userId);
    }


}
