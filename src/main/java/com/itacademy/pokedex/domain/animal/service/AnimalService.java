package com.itacademy.pokedex.domain.animal.service;

import com.itacademy.pokedex.domain.animal.dto.AnimalDto;
import com.itacademy.pokedex.domain.animal.exception.AnimalNotFoundException;
import com.itacademy.pokedex.domain.animal.mapper.AnimalMapper;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import org.springframework.stereotype.Service;

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

    public List<AnimalDto> searchAnimal(String animalName, Long userId) {

        List<Animal> animals = animalRepository
                .findByCommonNameContainingIgnoreCase(animalName);

        if (animals.isEmpty()) {
            throw new AnimalNotFoundException(animalName);
        }

        return animals.stream()
                .map(animal -> {
                    if (userId == null || !userAnimalRepository.existsByUserIdAndAnimalIdAndStatus(userId, animal.getId(), AnimalStatus.UNLOCK)) {
                        return animalMapper.toLockDto(animal);
                    } else {
                        return animalMapper.toUnlockDto(animal);
                    }
                })
                .collect(Collectors.toList());
    }

    public Animal getAnimalById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal no trobat amb ID: " + id));
    }
}
