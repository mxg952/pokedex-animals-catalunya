package com.itacademy.pokedex.domain.useranimal.service;

import com.itacademy.pokedex.domain.useranimal.dto.UnlockRequest;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.useranimal.modelo.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.modelo.entity.UserAnimal;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import com.itacademy.pokedex.domain.animal.exception.AnimalNotFoundException;
import com.itacademy.pokedex.domain.useranimal.exception.UserAnimalNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnimalService {

    private final AnimalRepository animalRepository;
    private final UserAnimalRepository userAnimalRepository;

    public UserAnimalService(AnimalRepository animalRepository, UserAnimalRepository userAnimalRepository) {
        this.animalRepository = animalRepository;
        this.userAnimalRepository = userAnimalRepository;
    }

    public UserAnimal unlockAnimal(Long userId, UnlockRequest request) {
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

    public List<UserAnimal> getUserAnimals(Long userId) {
        return animalRepository.findByUserId(userId);
    }
}
