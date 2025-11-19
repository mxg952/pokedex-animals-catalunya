package com.itacademy.pokedex.domain.useranimal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnimalPhotoDto {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String description;
    private LocalDateTime uploadedAt;
    private String downloadUrl;
    private String displayUrl;
    private Long userAnimalId;
    private Long animalId;
    private String animalCommonName;
}