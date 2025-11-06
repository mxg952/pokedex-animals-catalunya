package com.itacademy.pokedex.domain.animal;

import com.itacademy.pokedex.domain.animal.dto.AnimalDto;
import com.itacademy.pokedex.domain.animal.dto.AnimalUnlockDto;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.animal.service.AnimalService;
import com.itacademy.pokedex.domain.useranimal.modelo.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UserAnimalRepository userAnimalRepository;

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

        // Mock del repositorio
        when(animalRepository.findByCommonNameContainingIgnoreCase(animalName))
                .thenReturn(List.of(animal));
        when(userAnimalRepository.existsByUserIdAndAnimalIdAndStatus(userId, 1L, AnimalStatus.UNLOCK))
                .thenReturn(true); // Animal DESBLOQUEADO

        // When
        List<AnimalDto> result = animalService.searchAnimal(animalName, userId);

        // Then
        assertThat(result).hasSize(1);
        AnimalDto animalDto = result.get(0);

        // Verifica que es AnimalUnlockDto y haz casting
        assertThat(animalDto).isInstanceOf(AnimalUnlockDto.class);
        AnimalUnlockDto unlockDto = (AnimalUnlockDto) animalDto;

        // Ahora puedes acceder a getScientificName()
        assertThat(unlockDto.getCommonName()).isEqualTo("Lleó");
        assertThat(unlockDto.getScientificName()).isEqualTo("Pantera Leo");
        assertThat(unlockDto.getCategory()).isEqualTo("Mammal");
        assertThat(unlockDto.getPhotoUnlockUrl()).isEqualTo("http://photo-unlock.jpg");
        // Verifica otros campos específicos de UnlockDto
    }

    @Test
    void givenNonExistingName_whenSearch_thenThrowAnimalNotFoundException() {
        when(animalRepository.findByNomColoquialContainingIgnoreCase("Drac"))
                .thenReturn(List.of());

        assertThatThrownBy(() -> animalService.findByCommonName("Drac"))
                .isInstanceOf(AnimalNotFoundException.class)
                .hasMessageContaining("Drac");
    }
}
