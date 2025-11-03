package com.itacademy.pokedex.service;

import com.itacademy.pokedex.exceptions.AnimalNotFoundException;
import com.itacademy.pokedex.exceptions.UserAnimalNotFound;
import com.itacademy.pokedex.model.dto.request.UnlockAnimalRequest;
import com.itacademy.pokedex.model.entity.Animal;
import com.itacademy.pokedex.model.entity.AnimalStatus;
import com.itacademy.pokedex.model.entity.UserAnimal;
import org.springframework.stereotype.Service;
import repository.AnimalRepository;
import repository.UserAnimalRepository;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final UserAnimalRepository userAnimalRepository;

    public AnimalService(AnimalRepository animalRepository, UserAnimalRepository userAnimalRepository) {
        this.animalRepository = animalRepository;
        this.userAnimalRepository = userAnimalRepository;
    }

    public UserAnimal unlockAnimal(Long userId, UnlockAnimalRequest request) {
        Animal animal = animalRepository.findByCommonName(request.getCommonName())
                .orElseThrow(() -> new AnimalNotFoundException("L'animal no s'ha trobat"));

        UserAnimal userAnimal = userAnimalRepository.findByUserIdAndAnimalId(userId, animal.getId())
                .orElseThrow(() -> new UserAnimalNotFound("L'animal no s'ha trobat"));

        userAnimal.setStatus(AnimalStatus.UNLOCK);

        if (request.getPhotoUrl() != null && !request.getPhotoUrl().isEmpty()) {
            // Aquí només guardem el nom de la foto per simplificar
            // En una app real, aquí guardaries el fitxer a un directori o servei cloud
            userAnimal.setPhotoUrl(request.getPhotoUrl().getOriginalFilename());
        }

        return userAnimalRepository.save(userAnimal);
    }

    public List<UserAnimal> findUserAnimals(Long userId) {
         return animalRepository.findByUserId(userId);
    }




}
