package com.itacademy.pokedex.domain.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnimalRequest {

    @NotBlank(message = "El nom comú és obligatori")
    @Size(min = 2, max = 100, message = "El nom comú ha de tenir entre 2 i 100 caràcters")
    private String commonName;

    @Size(max = 150, message = "El nom científic no pot superar els 150 caràcters")
    private String scientificName;

    @Size(max = 50, message = "La categoria no pot superar els 50 caràcters")
    private String category;

    @Size(max = 20, message = "La probabilitat de visibilitat no pot superar els 20 caràcters")
    private String visibilityProbability;

    private List<String> sightingMonths;

    private String shortDescription;

    private String locationDescription;

    private String mapUrl;

    private String photoLockFileName;

    private String photoUnlockFileName;
}
