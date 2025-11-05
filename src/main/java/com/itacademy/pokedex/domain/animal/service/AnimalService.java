package com.itacademy.pokedex.domain.animal.service;

import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import org.springframework.stereotype.Service;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository, UserAnimalRepository userAnimalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }
}
