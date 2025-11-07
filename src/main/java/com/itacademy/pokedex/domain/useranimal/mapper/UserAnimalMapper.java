package com.itacademy.pokedex.domain.useranimal.mapper;

import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalDto;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalPhotoDto;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimal;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserAnimalMapper {

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public UserAnimalDto toDto(UserAnimal userAnimal) {
        if (userAnimal == null) {
            return null;
        }

        // Map de les fotos
        List<UserAnimalPhotoDto> photoDtos = userAnimal.getPhotos() != null ?
                userAnimal.getPhotos().stream()
                        .map(this::toPhotoDto)
                        .collect(Collectors.toList()) :
                List.of();

        // Construir el DTO base
        UserAnimalDto.UserAnimalDtoBuilder builder = UserAnimalDto.builder()
                .id(userAnimal.getId())
                .userId(userAnimal.getUserId())
                .animalId(userAnimal.getAnimalId())
                .status(userAnimal.getStatus())
                .unlockedAt(userAnimal.getUnlockedAt())
                .createdAt(userAnimal.getCreatedAt())
                .updatedAt(userAnimal.getUpdatedAt())
                .photos(photoDtos)
                .totalPhotos(photoDtos.size());

        // Afegir informació de l'animal si està disponible
        if (userAnimal.getAnimal() != null) {
            Animal animal = userAnimal.getAnimal();
            builder.animalCommonName(animal.getCommonName())
                    .animalScientificName(animal.getScientificName())
                    .animalCategory(animal.getCategory())
                    .animalDefaultPhotoUrl(animal.getPhotoUnlockUrl());
        }

        // Format de data per al frontend
        if (userAnimal.getUnlockedAt() != null) {
            builder.firstUnlockDate(userAnimal.getUnlockedAt()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        return builder.build();
    }

    public UserAnimalPhotoDto toPhotoDto(UserAnimalPhoto photo) {
        if (photo == null) {
            return null;
        }

        // Generar URLs per al frontend
        String downloadUrl = baseUrl + "/api/user-animals/photos/" + photo.getId() + "/download";
        String displayUrl = baseUrl + "/api/user-animals/photos/" + photo.getId() + "/display";

        UserAnimalPhotoDto.UserAnimalPhotoDtoBuilder builder = UserAnimalPhotoDto.builder()
                .id(photo.getId())
                .fileName(photo.getFileName())
                .originalFileName(photo.getOriginalFileName())
                .contentType(photo.getContentType())
                .fileSize(photo.getFileSize())
                .description(photo.getDescription())
                .uploadedAt(photo.getUploadedAt())
                .downloadUrl(downloadUrl)
                .displayUrl(displayUrl)
                .userAnimalId(photo.getUserAnimalId());

        // Afegir informació de l'animal si està disponible
        if (photo.getUserAnimal() != null && photo.getUserAnimal().getAnimal() != null) {
            builder.animalId(photo.getUserAnimal().getAnimal().getId())
                    .animalCommonName(photo.getUserAnimal().getAnimal().getCommonName());
        }

        return builder.build();
    }

    // Mètodes per a llistes
    public List<UserAnimalDto> toDtoList(List<UserAnimal> userAnimals) {
        return userAnimals.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<UserAnimalPhotoDto> toPhotoDtoList(List<UserAnimalPhoto> photos) {
        return photos.stream()
                .map(this::toPhotoDto)
                .collect(Collectors.toList());
    }
}
