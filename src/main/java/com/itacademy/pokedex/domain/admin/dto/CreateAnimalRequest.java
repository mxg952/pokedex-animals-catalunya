package com.itacademy.pokedex.domain.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnimalRequest {

    @NotBlank(message = "El nom comú és obligatori")
    private String commonName;

    @NotBlank(message = "El nom scientífic és obligatori")
    private String scientificName;

    private String category;
    private String visibilityProbability;
    private String sightingMonths;
    private String shortDescription;
    private String locationDescription;
    private String mapUrl;
    private String photoLockFileName;
    private String photoUnlockFileName;
    private MultipartFile lockedImage;
    private MultipartFile unlockedImage;
}