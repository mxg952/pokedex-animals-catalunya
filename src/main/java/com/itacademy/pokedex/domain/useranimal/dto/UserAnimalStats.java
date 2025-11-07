package com.itacademy.pokedex.domain.useranimal.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAnimalStats {
    private Long totalUnlockedAnimals;
    private Long totalPhotos;
    private Long totalAnimals; // Si tens el total d'animals disponibles
}
