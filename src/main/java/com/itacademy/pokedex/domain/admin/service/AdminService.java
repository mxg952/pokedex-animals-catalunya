package com.itacademy.pokedex.domain.admin.service;

import com.itacademy.pokedex.domain.admin.dto.AdminStatsDto;
import com.itacademy.pokedex.domain.admin.exception.PhotoDeletionException;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalDto;
import com.itacademy.pokedex.domain.useranimal.exception.PhotoNotFoundException;
import com.itacademy.pokedex.domain.useranimal.mapper.UserAnimalMapper;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalPhotoRepository;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserAnimalRepository userAnimalRepository;
    private final UserAnimalPhotoRepository userAnimalPhotoRepository;
    private final UserRepository userRepository;
    private final UserAnimalMapper userAnimalMapper;

    public AdminStatsDto getStats() {
        return AdminStatsDto.builder()
                .totalUsers(getTotalUsers())
                .totalUnlockedAnimals(getTotalUnlockedAnimals())
                .totalPhotos(getTotalPhotos())
                .generatedAt(generateTimestamp())
                .build();
    }

    // 🔹 Mètodes privats amb responsabilitats úniques
    private Long getTotalUsers() {
        return userRepository.count();
    }

    private Long getTotalUnlockedAnimals() {
        return userAnimalRepository.count();
    }

    private Long getTotalPhotos() {
        return userAnimalPhotoRepository.count();
    }

    private String generateTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void deleteUserPhoto(Long photoId) {
        UserAnimalPhoto photo = userAnimalPhotoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));

        try {
            // fileStorageService.deleteFile(photo.getPhotoUrl());
            userAnimalPhotoRepository.delete(photo);
            log.info("Foto eliminada correctament: {}", photoId);
        } catch (Exception e) {
            log.error("Error eliminant foto {}: {}", photoId, e.getMessage());
            throw new PhotoDeletionException("Error eliminant la foto");
        }
    }

    public List<UserAnimalDto> getAllUserAnimals() {
        return userAnimalRepository.findAll().stream()
                .map(userAnimalMapper::toDto)
                .toList();
    }
}
