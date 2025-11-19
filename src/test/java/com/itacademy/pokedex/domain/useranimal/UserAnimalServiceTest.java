package com.itacademy.pokedex.domain.useranimal;

import com.itacademy.pokedex.domain.animal.exception.AnimalNotFoundException;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.useranimal.dto.UnlockAnimalRequest;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalDto;
import com.itacademy.pokedex.domain.useranimal.mapper.UserAnimalMapper;
import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimal;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalPhotoRepository;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import com.itacademy.pokedex.domain.useranimal.service.FileStorageService;
import com.itacademy.pokedex.domain.useranimal.service.UserAnimalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAnimalServiceTest {

    @Mock
    private UserAnimalRepository userAnimalRepository;

    @Mock
    private UserAnimalPhotoRepository userAnimalPhotoRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private UserAnimalMapper userAnimalMapper;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private UserAnimalService userAnimalService;

    @Test
    void givenValidRequest_whenUnlockAnimal_thenReturnUserAnimalDto() {
        Long userId = 1L;
        String commonName = "Lleó";
        Long animalId = 1L;

        MultipartFile file = mock(MultipartFile.class);

        UnlockAnimalRequest request = UnlockAnimalRequest.builder()
                .commonName(commonName)
                .file(file)
                .description("Foto al parc")
                .build();

        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setCommonName(commonName);

        UserAnimal userAnimal = UserAnimal.builder()
                .id(1L)
                .userId(userId)
                .animalId(animalId)
                .status(AnimalStatus.UNLOCK)
                .build();

        UserAnimalPhoto photo = UserAnimalPhoto.builder()
                .id(1L)
                .fileName("photo123.jpg")
                .originalFileName("my_photo.jpg")
                .userAnimalId(1L)
                .build();

        UserAnimalDto expectedDto = UserAnimalDto.builder()
                .id(1L)
                .userId(userId)
                .animalId(animalId)
                .status(AnimalStatus.UNLOCK)
                .build();

        when(animalRepository.findByCommonName(commonName)).thenReturn(Optional.of(animal));
        when(userAnimalRepository.findByUserIdAndAnimalId(userId, animalId)).thenReturn(Optional.empty());
        when(userAnimalRepository.save(any(UserAnimal.class))).thenReturn(userAnimal);
        when(fileStorageService.storeFile(any(), any(), any())).thenReturn(photo);
        when(userAnimalPhotoRepository.save(any(UserAnimalPhoto.class))).thenReturn(photo);
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(userAnimalMapper.toDto(userAnimal, animal)).thenReturn(expectedDto);
        UserAnimalDto result = userAnimalService.unlockAnimal(userId, request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(AnimalStatus.UNLOCK);
    }

    @Test
    void givenNonExistentAnimalName_whenUnlockAnimal_thenThrowAnimalNotFoundException() {
        Long userId = 1L;
        String commonName = "AnimalInexistent";

        MultipartFile file = mock(MultipartFile.class);

        UnlockAnimalRequest request = UnlockAnimalRequest.builder()
                .commonName(commonName)
                .file(file)
                .build();

        when(animalRepository.findByCommonName(commonName)).thenReturn(Optional.empty());

        assertThrows(AnimalNotFoundException.class, () -> {
            userAnimalService.unlockAnimal(userId, request);
        });
    }
}
