package com.itacademy.pokedex.domain.animal.modelo.entity;

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
    private Long id;
    private String commonName;
    private String scientificName;
    private String category;
    private String visibilityProbability;

    @ElementCollection
    @CollectionTable(name = "animal_sighting_months", joinColumns = @JoinColumn(name = "animal_id"))
    @Column(name = "month")
    private List<String> sightingMonths;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String locationDescription;

    @Column(columnDefinition = "TEXT")
    private String mapUrl;

    private String photoLockFileName;
    private String photoUnlockFileName;
}


