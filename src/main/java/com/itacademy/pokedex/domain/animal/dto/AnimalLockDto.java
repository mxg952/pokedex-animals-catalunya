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
    private String category;              // ✅ AFEGEIX
    private String visibilityProbability; // ✅ AFEGEIX
    private List<String> sightingMonths;
    private String shortDescription;      // ✅ AFEGEIX
    private String locationDescription;
    private String mapUrl;
    private String photoLockUrl;
    private String photoUnlockUrl;        // ✅ AFEGEIX (per al visualitzador)
    private String message;
    private Boolean isLocked;             // ✅ AFEGEIX (calculat)
}