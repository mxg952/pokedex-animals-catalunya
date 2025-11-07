package com.itacademy.pokedex.domain.animal;

import com.itacademy.pokedex.domain.animal.dto.AnimalDto;
import com.itacademy.pokedex.domain.animal.dto.AnimalLockDto;
import com.itacademy.pokedex.domain.animal.dto.AnimalUnlockDto;
import com.itacademy.pokedex.domain.animal.exception.AnimalNotFoundException;
import com.itacademy.pokedex.domain.animal.mapper.AnimalMapper;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.animal.service.AnimalService;
import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UserAnimalRepository userAnimalRepository;

    @Mock
    private AnimalMapper animalMapper;

    @InjectMocks
    private AnimalService animalService;

    @Test
    void givenExistingAnimals_whenGetAllAnimals_thenReturnAnimalList() {
        Animal animal = new Animal();
        animal.setId(10L);
        animal.setCommonName("Gat");

        when(animalService.getAllAnimals()).thenReturn(List.of(animal));

        List<Animal> result = animalService.getAllAnimals();

        assertEquals(1, result.size());
        assertEquals("Gat", result.get(0).getCommonName());
        assertEquals(10L, result.get(0).getId());
    }

    @Test
    void givenUnlockedAnimal_whenSearch_thenReturnFullInfo() {
        // Given
        String animalName = "Lleó";
        Long userId = 1L;

        Animal animal = new Animal();
        animal.setId(1L);
        animal.setCommonName("Lleó");
        animal.setScientificName("Pantera Leo");
        animal.setCategory("Mammal");
        animal.setVisibilityProbability("Alta");
        animal.setSightingMonths(List.of("Enero", "Febrero", "Marzo"));
        animal.setShortDescription("Gran felino africano");
        animal.setLocationDescription("Sabana africana");
        animal.setMapUrl("http://map.example.com");
        animal.setPhotoUnlockUrl("http://photo-unlock.jpg");
        animal.setPhotoLockUrl("http://photo-lock.jpg");

        // Crea los DTOs que el mapper debería retornar
        AnimalUnlockDto unlockDto = AnimalUnlockDto.builder()
                .commonName("Lleó")
                .scientificName("Pantera Leo")
                .category("Mammal")
                .visibilityProbability("Alta")
                .sightingMonths(List.of("Enero", "Febrero", "Marzo"))
                .shortDescription("Gran felino africano")
                .locationDescription("Sabana africana")
                .mapUrl("http://map.example.com")
                .photoUnlockUrl("http://photo-unlock.jpg")
                .build();

        // Mock del repositorio
        when(animalRepository.findByCommonNameContainingIgnoreCase(animalName))
                .thenReturn(List.of(animal));
        when(userAnimalRepository.existsByUserIdAndAnimalIdAndStatus(userId, 1L, AnimalStatus.UNLOCK))
                .thenReturn(true); // Animal DESBLOQUEADO

        when(animalMapper.toUnlockDto(animal)).thenReturn(unlockDto);

        List<AnimalDto> result = animalService.searchAnimal(animalName, userId);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isInstanceOf(AnimalUnlockDto.class);

        AnimalUnlockDto resultDto = (AnimalUnlockDto) result.getFirst();

        assertEquals("Lleó", resultDto.getCommonName());
        assertEquals("Pantera Leo", resultDto.getScientificName());
        assertEquals("Mammal", resultDto.getCategory());
    }

    @Test
    void givenLockedAnimal_whenSearch_thenReturnLessInfo() {
        String animalName = "Lleó";
        Long userId = 1L;

        Animal animal = new Animal();
        animal.setId(1L);
        animal.setCommonName("Lleó");
        animal.setScientificName("Pantera Leo");
        animal.setCategory("Mammal");
        animal.setVisibilityProbability("Alta");
        animal.setSightingMonths(List.of("Enero", "Febrero", "Marzo"));
        animal.setShortDescription("Gran felino africano");
        animal.setLocationDescription("Sabana africana");
        animal.setMapUrl("http://map.example.com");
        animal.setPhotoUnlockUrl("http://photo-unlock.jpg");
        animal.setPhotoLockUrl("http://photo-lock.jpg");

        AnimalLockDto animalLockDto = new AnimalLockDto();
        animalLockDto.setCommonName("Lleó");
        animalLockDto.setScientificName("Pantera Leo");
        animalLockDto.setSightingMonths(List.of("Enero", "Febrero", "Marzo"));
        animalLockDto.setLocationDescription("Sabana afrincana");
        animalLockDto.setMapUrl("http://map.example.com");
        animalLockDto.setPhotoLockUrl("http://photo-lock.jpg");

        when(animalRepository.findByCommonNameContainingIgnoreCase(animalName))
                .thenReturn(List.of(animal));
        when(userAnimalRepository.existsByUserIdAndAnimalIdAndStatus(userId, animal.getId(), AnimalStatus.UNLOCK))
                .thenReturn(false);
        when(animalMapper.toLockDto(animal)).thenReturn(animalLockDto);

        List<AnimalDto> result = animalService.searchAnimal(animalName, userId);

        assertEquals(1, result.size());
        assertThat(result.getFirst()).isInstanceOf(AnimalLockDto.class);

        AnimalLockDto resultDto = (AnimalLockDto) result.getFirst();

        assertEquals("Lleó", resultDto.getCommonName());
        assertEquals("Pantera Leo", resultDto.getScientificName());
        assertEquals(List.of("Enero", "Febrero", "Marzo"), resultDto.getSightingMonths());


    }

    @Test
    void givenNonExistingName_whenSearch_thenThrowAnimalNotFoundException() {
        Long userId = 1L;

        when(animalRepository.findByCommonNameContainingIgnoreCase("Drac"))
                .thenReturn(List.of());

        assertThatThrownBy(() -> animalService.searchAnimal("Drac", userId))
                .isInstanceOf(AnimalNotFoundException.class)
                .hasMessageContaining("No s'ha trobat l'animal...");
    }
}
