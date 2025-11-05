package com.itacademy.pokedex.domain.animal;

import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.animal.service.AnimalService;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Mockito.when(animalService.getAllAnimals()).thenReturn(List.of(animal));

        List<Animal> result = animalService.getAllAnimals();

        assertEquals(1, result.size());
        assertEquals("Gat", result.get(0).getCommonName());
        assertEquals(10L, result.get(0).getId());
    }
}
