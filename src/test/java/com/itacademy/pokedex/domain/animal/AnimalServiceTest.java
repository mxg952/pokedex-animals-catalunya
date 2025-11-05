package com.itacademy.pokedex.domain.animal;

import com.itacademy.pokedex.domain.animal.entity.Animal;
import com.itacademy.pokedex.domain.animal.service.AnimalService;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.exceptions.AnimalNotFoundException;
import com.itacademy.pokedex.domain.animal.dto.UnlockAnimalRequest;
import com.itacademy.pokedex.domain.useranimal.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.UserAnimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.itacademy.pokedex.domain.useranimal.UserAnimalRepository;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
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

        UnlockAnimalRequest request = new UnlockAnimalRequest();
        request.setCommonName(animalName);
        request.setPhotoUrl(photo);

        Mockito.when(animalRepository.findByCommonName(animalName)).thenReturn(Optional.of(animal));
        Mockito.when(userAnimalRepository.findByUserIdAndAnimalId(userId, animal.getId()))
                .thenReturn(Optional.of(userAnimalLock));

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

    @Test
    void givenUnlockAnimalRequest_whenAnimalNotFound_thenExceptionShouldBeenThrown() {
        Long userId = 1L;
        String animalName = "Senglar";
        MultipartFile photo = new MockMultipartFile("foto", "senglar.jpg", "image/jpeg",
                "dummy".getBytes());

        UnlockAnimalRequest request = new UnlockAnimalRequest();
        request.setCommonName(animalName);
        request.setPhotoUrl(photo);

        Mockito.when(animalRepository.findByCommonName(animalName))
                .thenReturn(Optional.empty());

        assertThrows(AnimalNotFoundException.class, () -> animalService.unlockAnimal(userId, request));
    }

    @Test
    void givenUser_whenGetUserAnimals_thenReturnUserAnimals() {
        Animal animal = new Animal();
        animal.setId(10L);
        animal.setCommonName("Senglar");

        UserAnimal userAnimal = new UserAnimal();
        userAnimal.setUserId(1L);
        userAnimal.setAnimal(animal);
        userAnimal.setStatus(AnimalStatus.UNLOCK);

        Mockito.when(animalService.getUserAnimals(1L)).thenReturn(List.of(userAnimal));

        List<UserAnimal> result = animalService.getUserAnimals(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals("Senglar", result.get(0).getAnimal().getCommonName());
        assertEquals(AnimalStatus.UNLOCK, result.get(0).getStatus());
    }

    @Test
    void givenExistingAnimals_whenGetAllAnimals_thenReturnAnimalList() {
        Animal animal = new Animal();
        animal.setId(10L);
        animal.setCommonName("Gat");

        Mockito.when(animalService.getAllAnimals()).thenReturn(List.of(animal));

        List<Animal> result = animalService.getAllAnimals();

        assertEquals(1, result.size());
        assertEquals("Gat", result.get(0).getCommonName());
        assertEquals(10L,result.get(0).getId());
    }



}
