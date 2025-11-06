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
    private String commonName;           // Nom coloquial
    private String scientificName;       // Nom científic
    private List<String> sightingMonths; // Months of the year when it is usually seen
    private String locationDescription;  // Description of the habitat/location
    private String mapUrl;
    private String photoLockUrl;
    private String message;

}
