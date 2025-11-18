package com.itacademy.pokedex.domain.animal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnimalLockDto implements AnimalDto {
    private Long id;
    private String commonName;
    private String scientificName;
    private String category;
    private String visibilityProbability;
    private List<String> sightingMonths;
    private String shortDescription;
    private String locationDescription;
    private String mapUrl;
    private String photoLockUrl;
    private String photoUnlockUrl;
    private String message;
    private Boolean isLocked;
}