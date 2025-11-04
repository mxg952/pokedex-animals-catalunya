package com.itacademy.pokedex.domain.animal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                     // Primary key
    private String commonName;           // Nom coloquial
    private String scientificName;       // Nom científic
    private String category;             // Category (mammal, bird, reptile, etc.)
    private String visibilityProbability; // Probability of sighting (low, normal, high)
    private List<String> sightingMonths; // Months of the year when it is usually seen
    private String shortDescription;     // Brief description of the animal
    private String locationDescription;  // Description of the habitat/location
    private String mapUrl;               // URL of map showing the location(s)
}


