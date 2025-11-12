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

    // Dates importants
    private LocalDateTime unlockedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Llista de fotos de l'usuari per aquest animal
    @Builder.Default
    private List<UserAnimalPhotoDto> photos = new ArrayList<>();

    // Informació de l'animal (per evitar fer més consultes al frontend)
    private String animalCommonName;
    private String animalScientificName;
    private String animalCategory;
    private String animalDefaultPhotoUrl; // Foto predefinida de l'animal

    // Estadístiques (calculades)
    private Integer totalPhotos;
    private String firstUnlockDate; // Data en format string per al frontend
    private String displayName;

    // ✅ CORREGIR: Cambiar Boolean por boolean o calcularlo correctamente
    private boolean unlocked; // ← CAMBIAR de Boolean a boolean

    private String mainPhotoUrl;

    // ✅ MÉTODO CORREGIDO: Asegurar que se llama en el mapper
    public boolean isUnlocked() {
        return status == AnimalStatus.UNLOCK;
    }

    public boolean hasPhotos() {
        return photos != null && !photos.isEmpty();
    }

    public String getDisplayName() {
        return animalCommonName != null ? animalCommonName : "Animal " + animalId;
    }

    public String getMainPhotoUrl() {
        if (hasPhotos()) {
            return photos.get(0).getFileName();
        }
        return animalDefaultPhotoUrl;
    }
}