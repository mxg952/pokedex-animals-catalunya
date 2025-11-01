package com.itacademy.pokedex.service;

import com.itacademy.pokedex.model.dto.request.AnimalRequest;
import com.itacademy.pokedex.model.entity.Animal;
import com.itacademy.pokedex.model.entity.AnimalStatus;
import com.itacademy.pokedex.model.entity.UserAnimal;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import repository.AnimalRepository;
import repository.UserAnimalRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UserAnimalRepository userAnimalRepository;

    @InjectMocks
    private AnimalService animalService;

    @Test
    void givenLockAnimal_whenUserSendNameAndPhoto_thenAnimalShouldUnlock() {
    Long userId = 1L;
    String animalName = "Senglar";
    MultipartFile photo = new MockMultipartFile("foto", "senglar.jpg", "image/jpeg",
            "dummy".getBytes());

        Animal animal = new Animal();
        animal.setId(10L);
        animal.setCommonName(animalName);

        UserAnimal userAnimalLock = new UserAnimal();
        userAnimalLock.setUserId(userId);
        userAnimalLock.setAnimal(animal);
        userAnimalLock.setStatus(AnimalStatus.LOCK);

        AnimalRequest request = new AnimalRequest();
        request.setCommonName(animalName);
        request.setPhotoUrl(photo);

        Mockito.when(animalRepository.findByCommonName(animalName)).thenReturn(Optional.of(animal));
        Mockito.when(userAnimalRepository.findByUserIdAndAnimalId(userId, animal.getId())).thenReturn(Optional.of(userAnimalLock));

        // Quan guardem l'entitat UserAnimal, simplement retornem la mateixa entitat
        Mockito.when(userAnimalRepository.save(any(UserAnimal.class)))
                .thenAnswer(invocation -> {
                    // L'argument 0 és l'objecte UserAnimal que s'està guardant
                    UserAnimal userAnimalGuardat = invocation.getArgument(0);
                    return userAnimalGuardat;
                });

        // Act
        UserAnimal result = animalService.unlockAnimal(userId, request);

        // Assert
        assertEquals(AnimalStatus.UNLOCK, result.getStatus());
        assertEquals("senglar.jpg", result.getPhotoUrl());
        assertEquals(userId, result.getUserId());
        assertEquals(animal, result.getAnimal());

    }

}
