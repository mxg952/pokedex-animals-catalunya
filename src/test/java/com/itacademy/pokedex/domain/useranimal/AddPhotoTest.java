package com.itacademy.pokedex.domain.useranimal;

import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalDto;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalPhotoDto;
import com.itacademy.pokedex.domain.useranimal.exception.AnimalNotUnlockedException;
import com.itacademy.pokedex.domain.useranimal.exception.InvalidFileException;
import com.itacademy.pokedex.domain.useranimal.exception.UserAnimalNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddPhotoTest {

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
    void givenUnlockedAnimal_whenAddPhoto_thenReturnUserAnimalWithPhotos() {
        // GIVEN
        Long userId = 1L;
        Long animalId = 1L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");

        String description = "Nova foto del lleó";

        UserAnimal userAnimal = UserAnimal.builder()
                .id(1L)
                .userId(userId)
                .animalId(animalId)
                .status(AnimalStatus.UNLOCK)
                .photos(new ArrayList<>()) // Llista buida inicialment
                .build();

        UserAnimalPhoto newPhoto = UserAnimalPhoto.builder()
                .id(2L)
                .fileName("new_photo.jpg")
                .originalFileName("my_new_photo.jpg")
                .description(description)
                .userAnimalId(1L)
                .build();

        UserAnimalDto expectedDto = UserAnimalDto.builder()
                .id(1L)
                .photos(List.of(
                        UserAnimalPhotoDto.builder()
                                .id(2L)
                                .fileName("new_photo.jpg")
                                .description(description)
                                .build()
                ))
                .build();

        // WHEN
        when(userAnimalRepository.findByUserIdAndAnimalId(userId, animalId))
                .thenReturn(Optional.of(userAnimal));
        when(fileStorageService.storeFile(file, description, userAnimal.getId()))
                .thenReturn(newPhoto);
        when(userAnimalPhotoRepository.save(newPhoto)).thenReturn(newPhoto);
        when(userAnimalMapper.toDto(userAnimal)).thenReturn(expectedDto);

        UserAnimalDto result = userAnimalService.addPhoto(userId, animalId, file, description);

        // THEN
        assertThat(result).isNotNull();
        assertEquals(1, result.getPhotos().size());
        assertThat(result.getPhotos().getFirst().getDescription()).isEqualTo(description);

        verify(userAnimalPhotoRepository).save(newPhoto);
        verify(fileStorageService).storeFile(file, description, userAnimal.getId());
    }

    @Test
    void givenLockedAnimal_whenAddPhoto_thenThrowAnimalNotUnlockedException() {
        // GIVEN
        Long userId = 1L;
        Long animalId = 1L;

        MultipartFile file = mock(MultipartFile.class);

        UserAnimal userAnimal = UserAnimal.builder()
                .id(1L)
                .userId(userId)
                .animalId(animalId)
                .status(AnimalStatus.LOCK) // ❌ BLOQUEJAT
                .build();

        // WHEN & THEN
        when(userAnimalRepository.findByUserIdAndAnimalId(userId, animalId))
                .thenReturn(Optional.of(userAnimal));

        assertThrows(AnimalNotUnlockedException.class, () -> {
            userAnimalService.addPhoto(userId, animalId, file, "Descripció");
        });

        verify(userAnimalPhotoRepository, never()).save(any());
        verify(fileStorageService, never()).storeFile(any(), any(), any());
    }

    @Test
    void givenNonExistentUserAnimal_whenAddPhoto_thenThrowUserAnimalNotFoundException() {
        // GIVEN
        Long userId = 1L;
        Long animalId = 999L;

        MultipartFile file = mock(MultipartFile.class);

        // WHEN & THEN
        when(userAnimalRepository.findByUserIdAndAnimalId(userId, animalId))
                .thenReturn(Optional.empty());

        assertThrows(UserAnimalNotFoundException.class, () -> {
            userAnimalService.addPhoto(userId, animalId, file, "Descripció");
        });

        verify(userAnimalPhotoRepository, never()).save(any());
        verify(fileStorageService, never()).storeFile(any(), any(), any());
    }

    @Test
    void givenEmptyFile_whenAddPhoto_thenThrowInvalidFileException() {
        // GIVEN
        Long userId = 1L;
        Long animalId = 1L;

        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true); // ✅ Fitxer buit

        UserAnimal userAnimal = UserAnimal.builder()
                .id(1L)
                .userId(userId)
                .animalId(animalId)
                .status(AnimalStatus.UNLOCK)
                .build();

        // WHEN & THEN - ✅ SOLO el mock necessari
        when(userAnimalRepository.findByUserIdAndAnimalId(userId, animalId))
                .thenReturn(Optional.of(userAnimal));

        // ❌ NO configurar fileStorageService.storeFile - no s'utilitzarà
        // ❌ NO configurar userAnimalPhotoRepository.save - no s'utilitzarà

        assertThrows(InvalidFileException.class, () -> {
            userAnimalService.addPhoto(userId, animalId, emptyFile, "Descripció");
        });

        // Verificar que NO es van cridar els mètodes
        verify(fileStorageService, never()).storeFile(any(), any(), any());
        verify(userAnimalPhotoRepository, never()).save(any());
    }

    @Test
    void givenNonImageFile_whenAddPhoto_thenThrowInvalidFileException() {
        // GIVEN
        Long userId = 1L;
        Long animalId = 1L;

        MultipartFile textFile = mock(MultipartFile.class);

        UserAnimal userAnimal = UserAnimal.builder()
                .id(1L)
                .userId(userId)
                .animalId(animalId)
                .status(AnimalStatus.UNLOCK)
                .build();

        // WHEN & THEN - ✅ SOLO el mock necessari
        when(userAnimalRepository.findByUserIdAndAnimalId(userId, animalId))
                .thenReturn(Optional.of(userAnimal));

        // ❌ NO configurar fileStorageService.storeFile - no s'utilitzarà
        // ❌ NO configurar userAnimalPhotoRepository.save - no s'utilitzarà

        assertThrows(InvalidFileException.class, () -> {
            userAnimalService.addPhoto(userId, animalId, textFile, "Descripció");
        });

        // Verificar que NO es van cridar els mètodes
        verify(fileStorageService, never()).storeFile(any(), any(), any());
        verify(userAnimalPhotoRepository, never()).save(any());
    }

    @Test
    void givenUnlockedAnimalWithExistingPhotos_whenAddPhoto_thenAddToExistingPhotos() {
        // GIVEN
        Long userId = 1L;
        Long animalId = 1L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/png"); // ✅ CONFIGURAT com a imatge


        String description = "Tercera foto";

        // Foto existent
        UserAnimalPhoto existingPhoto = UserAnimalPhoto.builder()
                .id(1L)
                .fileName("existing_photo.jpg")
                .originalFileName("old_photo.jpg")
                .description("Foto existent")
                .userAnimalId(1L)
                .build();

        UserAnimalPhotoDto existingPhotoDto = UserAnimalPhotoDto.builder()
                .id(1L)
                .fileName("existing_photo.jpg")
                .originalFileName("old_photo.jpg")
                .description("Foto existent")
                .build();

        UserAnimal userAnimal = UserAnimal.builder()
                .id(1L)
                .userId(userId)
                .animalId(animalId)
                .status(AnimalStatus.UNLOCK)
                .photos(new ArrayList<>(List.of(existingPhoto))) // Ja té una foto
                .build();

        UserAnimalPhoto newPhoto = UserAnimalPhoto.builder()
                .id(2L)
                .fileName("new_photo_2.jpg")
                .originalFileName("another_photo.jpg")
                .description(description)
                .userAnimalId(1L)
                .build();

        UserAnimalPhotoDto newPhotoDto = UserAnimalPhotoDto.builder()
                .id(2L)
                .fileName("new_photo_2.jpg")
                .originalFileName("another_photo.jpg")
                .description(description)
                .build();

        UserAnimalDto expectedDto = UserAnimalDto.builder()
                .id(1L)
                .photos(List.of(existingPhotoDto, newPhotoDto)) // ← CORREGIT: Ara són DTOs
                .build();

        // WHEN
        when(userAnimalRepository.findByUserIdAndAnimalId(userId, animalId))
                .thenReturn(Optional.of(userAnimal));
        when(fileStorageService.storeFile(file, description, userAnimal.getId()))
                .thenReturn(newPhoto);
        when(userAnimalPhotoRepository.save(newPhoto)).thenReturn(newPhoto);
        when(userAnimalMapper.toDto(userAnimal)).thenReturn(expectedDto);

        UserAnimalDto result = userAnimalService.addPhoto(userId, animalId, file, description);

        // THEN
        assertThat(result).isNotNull();
        assertEquals(2, result.getPhotos().size());
        assertThat(result.getPhotos().get(1).getDescription()).isEqualTo(description);

        verify(userAnimalPhotoRepository).save(newPhoto);
    }
}
