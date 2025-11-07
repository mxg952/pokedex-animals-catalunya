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
public class AnimalUnlockDto implements AnimalDto {
    private String commonName;           // Nom coloquial
    private String scientificName;       // Nom científic
    private String category;             // Category (mammal, bird, reptile, etc.)
    private String visibilityProbability; // Probability of sighting (low, normal, high)
    private List<String> sightingMonths; // Months of the year when it is usually seen
    private String shortDescription;     // Brief description of the animal
    private String locationDescription;  // Description of the habitat/location
    private String mapUrl;
    private String photoUnlockUrl;

}
