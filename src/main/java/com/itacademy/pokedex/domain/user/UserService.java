package com.itacademy.pokedex.domain.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User getById(Long userId) {
        return userRepository.getReferenceById(userId);
    }


}
