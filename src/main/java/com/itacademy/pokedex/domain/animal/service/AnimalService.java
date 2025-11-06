package com.itacademy.pokedex.domain.animal.service;

import com.itacademy.pokedex.domain.animal.dto.AnimalDto;
import com.itacademy.pokedex.domain.animal.exception.AnimalNotFoundException;
import com.itacademy.pokedex.domain.animal.mapper.AnimalMapper;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.useranimal.modelo.AnimalStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final UserAnimalRepository userAnimalRepository;
    private final AnimalMapper animalMapper;

    public AnimalService(AnimalRepository animalRepository, UserAnimalRepository userAnimalRepository, AnimalMapper animalMapper) {
        this.animalRepository = animalRepository;
        this.userAnimalRepository = userAnimalRepository;
        this.animalMapper = animalMapper;
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public List<AnimalDto> searchAnimal(String name, Long userId) {

        List<Animal> animals = animalRepository
                .findByCommonNameContainingIgnoreCase(name);

        if (animals.isEmpty()) {
            throw new AnimalNotFoundException(name);
        }

        return animals.stream()
                .map(animal -> {
                    boolean unlocked = userAnimalRepository
                            .existsByUserIdAndAnimalIdAndStatus(userId, animal.getId(), AnimalStatus.UNLOCK);
                    return unlocked
                            ? animalMapper.toUnlockDto(animal) // informació completa
                            : animalMapper.toLockDto(animal); // informació parcial
                })
                .collect(Collectors.toList());
    }
}
