package com.itacademy.pokedex.domain.animal.mapper;

import com.itacademy.pokedex.domain.animal.dto.AnimalLockDto;
import com.itacademy.pokedex.domain.animal.dto.AnimalUnlockDto;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnimalMapper {

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public AnimalUnlockDto toUnlockDto(Animal animal) {
        String unlockImageUrl = baseUrl + "/api/images/animals/unlocked/" +
                (animal.getPhotoUnlockFileName() != null ? animal.getPhotoUnlockFileName() : "default_animal.jpg");

        log.debug("🔧 Generant URL imatge desbloquejada: {}", unlockImageUrl);

        return AnimalUnlockDto.builder()
                .commonName(animal.getCommonName())
                .scientificName(animal.getScientificName())
                .category(animal.getCategory())
                .visibilityProbability(animal.getVisibilityProbability())
                .sightingMonths(animal.getSightingMonths())
                .shortDescription(animal.getShortDescription())
                .locationDescription(animal.getLocationDescription())
                .mapUrl(animal.getMapUrl())
                .photoUnlockUrl(unlockImageUrl)
                .build();
    }

    public AnimalLockDto toLockDto(Animal animal) {
        String lockImageUrl = baseUrl + "/api/images/animals/locked/" +
                (animal.getPhotoLockFileName() != null ? animal.getPhotoLockFileName() : "default_silhouette.png");

        String unlockImageUrl = baseUrl + "/api/images/animals/unlocked/" +
                (animal.getPhotoUnlockFileName() != null ? animal.getPhotoUnlockFileName() : "default_animal.jpg");

        log.debug("🔧 Generant URLs per animal {}: locked={}, unlocked={}",
                animal.getId(), lockImageUrl, unlockImageUrl);

        return AnimalLockDto.builder()
                .id(animal.getId())
                .commonName(animal.getCommonName())
                .scientificName(animal.getScientificName())
                .category(animal.getCategory())
                .visibilityProbability(animal.getVisibilityProbability())
                .sightingMonths(animal.getSightingMonths())
                .shortDescription(animal.getShortDescription())
                .locationDescription(animal.getLocationDescription())
                .mapUrl(animal.getMapUrl())
                .photoLockUrl(lockImageUrl)
                .photoUnlockUrl(unlockImageUrl)
                .isLocked(true)
                .message("Desbloqueja per obtenir més informació!")
                .build();
    }
}