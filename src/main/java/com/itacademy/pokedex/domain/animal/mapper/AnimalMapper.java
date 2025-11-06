package com.itacademy.pokedex.domain.animal.mapper;

import com.itacademy.pokedex.domain.animal.dto.AnimalLockDto;
import com.itacademy.pokedex.domain.animal.dto.AnimalUnlockDto;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnimalMapper {

    public AnimalUnlockDto toUnlockDto(Animal animal) {
        return AnimalUnlockDto.builder()
                .commonName(animal.getCommonName())
                .scientificName(animal.getScientificName())
                .category(animal.getCategory())
                .visibilityProbability(animal.getVisibilityProbability())
                .sightingMonths(animal.getSightingMonths())
                .shortDescription(animal.getShortDescription())
                .locationDescription(animal.getLocationDescription())
                .mapUrl(animal.getMapUrl())
                .photoUnlockUrl(animal.getPhotoUnlockUrl())
                .build();
    }

    public AnimalLockDto toLockDto(Animal animal) {
        return AnimalLockDto.builder()
                .commonName(animal.getCommonName())
                .scientificName(animal.getScientificName())
                .sightingMonths(animal.getSightingMonths())
                .locationDescription(animal.getLocationDescription())
                .mapUrl(animal.getMapUrl())
                .photoLockUrl(animal.getPhotoLockUrl())
                .message("Desbloqueja per obtenir més informació!")
                .build();
    }
}
