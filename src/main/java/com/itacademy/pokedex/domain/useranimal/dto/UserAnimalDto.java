package com.itacademy.pokedex.domain.useranimal.dto;

import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnimalDto {
    private Long id;
    private Long userId;
    private Long animalId;
    private AnimalStatus status;
    private LocalDateTime unlockedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<UserAnimalPhotoDto> photos = new ArrayList<>();
    private String animalCommonName;
    private String animalScientificName;
    private String animalCategory;
    private String animalDefaultPhotoUrl;
    private Integer totalPhotos;
    private String firstUnlockDate;
    private String displayName;
    private boolean unlocked;
    private String mainPhotoUrl;
}