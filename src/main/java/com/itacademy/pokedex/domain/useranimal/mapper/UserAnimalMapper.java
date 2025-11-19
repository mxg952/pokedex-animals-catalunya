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

    public UserAnimalDto toDto(UserAnimal userAnimal, Animal animal) {
        if (userAnimal == null) {
            return null;
        }

        List<UserAnimalPhotoDto> photoDtos = userAnimal.getPhotos() != null ?
                userAnimal.getPhotos().stream()
                        .map(this::toPhotoDto)
                        .collect(Collectors.toList()) :
                List.of();

        boolean unlocked = userAnimal.getStatus() != null &&
                userAnimal.getStatus().name().equals("UNLOCK");

        UserAnimalDto.UserAnimalDtoBuilder builder = UserAnimalDto.builder()
                .id(userAnimal.getId())
                .userId(userAnimal.getUserId())
                .animalId(userAnimal.getAnimalId())
                .status(userAnimal.getStatus())
                .unlockedAt(userAnimal.getUnlockedAt())
                .createdAt(userAnimal.getCreatedAt())
                .updatedAt(userAnimal.getUpdatedAt())
                .photos(photoDtos)
                .totalPhotos(photoDtos.size())
                .unlocked(unlocked);

        if (animal != null) {
            builder.animalCommonName(animal.getCommonName())
                    .animalScientificName(animal.getScientificName())
                    .animalCategory(animal.getCategory())
                    .animalDefaultPhotoUrl(animal.getPhotoUnlockFileName())
                    .displayName(animal.getCommonName());
        } else {
            builder.displayName("Animal " + userAnimal.getAnimalId());
        }

        if (userAnimal.getUnlockedAt() != null) {
            builder.firstUnlockDate(userAnimal.getUnlockedAt()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (!photoDtos.isEmpty()) {
            builder.mainPhotoUrl(photoDtos.getFirst().getFileName());
        } else if (animal != null && animal.getPhotoUnlockFileName() != null) {
            builder.mainPhotoUrl(animal.getPhotoUnlockFileName());
        }

        System.out.println("🔍 UserAnimalMapper - Animal: " +
                (animal != null ? animal.getCommonName() : "null") +
                ", Status: " + userAnimal.getStatus() +
                ", Unlocked: " + unlocked);

        return builder.build();
    }

    public UserAnimalDto toDto(UserAnimal userAnimal) {
        return toDto(userAnimal, null);
    }

    public UserAnimalPhotoDto toPhotoDto(UserAnimalPhoto photo) {
        if (photo == null) {
            return null;
        }

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

        if (photo.getUserAnimal() != null && photo.getUserAnimal().getAnimal() != null) {
            builder.animalId(photo.getUserAnimal().getAnimal().getId())
                    .animalCommonName(photo.getUserAnimal().getAnimal().getCommonName());
        }

        return builder.build();
    }
}