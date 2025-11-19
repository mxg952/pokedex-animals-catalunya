package com.itacademy.pokedex.domain.admin.service;

import com.itacademy.pokedex.domain.admin.dto.AdminStatsDto;
import com.itacademy.pokedex.domain.admin.dto.CreateAnimalRequest;
import com.itacademy.pokedex.domain.admin.dto.UserInfoDto;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.domain.useranimal.mapper.UserAnimalMapper;
import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalPhotoRepository;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import com.itacademy.pokedex.domain.useranimal.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserAnimalRepository userAnimalRepository;
    private final UserAnimalPhotoRepository userAnimalPhotoRepository;
    private final UserRepository userRepository;
    private final UserAnimalMapper userAnimalMapper;
    private final AnimalRepository animalRepository;
    private final FileStorageService fileStorageService;

    public AdminStatsDto getStats() {
        return AdminStatsDto.builder()
                .totalUsers(getTotalUsers())
                .totalUnlockedAnimals(getTotalUnlockedAnimals())
                .totalPhotos(getTotalPhotos())
                .generatedAt(generateTimestamp())
                .build();
    }

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

    public List<UserInfoDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    Long unlockedAnimals = userAnimalRepository.countByUserIdAndStatus(
                            user.getId(),
                            AnimalStatus.UNLOCK
                    );
                    Long uploadedPhotos = userAnimalPhotoRepository.countByUserId(user.getId());

                    return UserInfoDto.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .createdAt(user.getCreatedAt())
                            .unlockedAnimals(unlockedAnimals)
                            .uploadedPhotos(uploadedPhotos)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Animal createAnimal(CreateAnimalRequest request) {
        String lockedImageFileName = null;
        String unlockedImageFileName = null;

        try {
            if (request.getLockedImage() != null && !request.getLockedImage().isEmpty()) {
                lockedImageFileName = fileStorageService.storeAnimalImage(request.getLockedImage(), "locked");
            }

            if (request.getUnlockedImage() != null && !request.getUnlockedImage().isEmpty()) {
                unlockedImageFileName = fileStorageService.storeAnimalImage(request.getUnlockedImage(), "unlocked");
            }

            List<String> sightingMonthsList = new ArrayList<>();
            if (request.getSightingMonths() != null && !request.getSightingMonths().trim().isEmpty()) {
                sightingMonthsList = Arrays.stream(request.getSightingMonths().split(","))
                        .map(String::trim)
                        .filter(month -> !month.isEmpty())
                        .collect(Collectors.toList());
            }

            Animal animal = Animal.builder()
                    .commonName(request.getCommonName())
                    .scientificName(request.getScientificName())
                    .category(request.getCategory())
                    .visibilityProbability(request.getVisibilityProbability())
                    .sightingMonths(sightingMonthsList)
                    .shortDescription(request.getShortDescription())
                    .locationDescription(request.getLocationDescription())
                    .mapUrl(request.getMapUrl())
                    .photoLockFileName(lockedImageFileName)
                    .photoUnlockFileName(unlockedImageFileName)
                    .build();

            Animal savedAnimal = animalRepository.save(animal);
            log.info("Nou animal creat: {} (ID: {})", savedAnimal.getCommonName(), savedAnimal.getId());

            return savedAnimal;

        } catch (Exception e) {
            log.error("Error creant animal: {}", e.getMessage(), e);

            if (lockedImageFileName != null) {
                fileStorageService.deleteAnimalImage("locked", lockedImageFileName);
            }
            if (unlockedImageFileName != null) {
                fileStorageService.deleteAnimalImage("unlocked", unlockedImageFileName);
            }
            throw new RuntimeException("Error creant animal: " + e.getMessage(), e);
        }
    }
}
