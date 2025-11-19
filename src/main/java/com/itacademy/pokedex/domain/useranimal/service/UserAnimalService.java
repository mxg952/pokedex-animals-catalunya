package com.itacademy.pokedex.domain.useranimal.service;

import com.itacademy.pokedex.domain.animal.exception.AnimalNotFoundException;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.useranimal.dto.UnlockAnimalRequest;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalDto;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalPhotoDto;
import com.itacademy.pokedex.domain.useranimal.exception.*;
import com.itacademy.pokedex.domain.useranimal.mapper.UserAnimalMapper;
import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimal;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalPhotoRepository;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserAnimalService {

    private final AnimalRepository animalRepository;
    private final UserAnimalRepository userAnimalRepository;
    private final FileStorageService fileStorageService;
    private final UserAnimalMapper userAnimalMapper;
    private final UserAnimalPhotoRepository userAnimalPhotoRepository;

    public UserAnimalService(AnimalRepository animalRepository, UserAnimalRepository userAnimalRepository,
                             FileStorageService fileStorageService, UserAnimalMapper userAnimalMapper,
                             UserAnimalPhotoRepository userAnimalPhotoRepository) {
        this.animalRepository = animalRepository;
        this.userAnimalRepository = userAnimalRepository;
        this.fileStorageService = fileStorageService;
        this.userAnimalMapper = userAnimalMapper;
        this.userAnimalPhotoRepository = userAnimalPhotoRepository;
    }

    public UserAnimalDto unlockAnimal(Long userId, UnlockAnimalRequest request) {
        Animal animal = animalRepository.findByCommonName(request.getCommonName())
                .orElseThrow(() -> new AnimalNotFoundException("L'animal no s'ha trobat..."));

        userAnimalRepository.findByUserIdAndAnimalId(userId, animal.getId())
                .ifPresent(ua -> {
                    throw new AnimalAlreadyUnlockedException("L'animal ja està desbloquejat...");
                });

        UserAnimal userAnimal = UserAnimal.builder()
                .userId(userId)
                .animalId(animal.getId())
                .status(AnimalStatus.UNLOCK)
                .build();

        UserAnimal savedUserAnimal = userAnimalRepository.save(userAnimal);
        UserAnimalPhoto unlockPhoto = fileStorageService.storeFile(request.getFile(), request.getDescription(), savedUserAnimal.getId());
        UserAnimalPhoto savedPhoto = userAnimalPhotoRepository.save(unlockPhoto);
        savedUserAnimal.addPhoto(savedPhoto);

        Animal completeAnimal = animalRepository.findById(animal.getId())
                .orElseThrow(() -> new AnimalNotFoundException("Animal no trobat"));

        return userAnimalMapper.toDto(savedUserAnimal, completeAnimal);
    }

    public List<UserAnimalDto> getUserAnimals(Long userId) {
        return userAnimalRepository.findByUserId(userId).stream()
                .map(userAnimal -> {
                    Animal animal = animalRepository.findById(userAnimal.getAnimalId()).orElse(null);
                    return userAnimalMapper.toDto(userAnimal, animal);
                })
                .collect(Collectors.toList());
    }

    public UserAnimalDto addPhoto(Long userId, Long animalId, MultipartFile file, String description) {
        UserAnimal userAnimal = getUserAnimalWithValidation(userId, animalId);
        validateImageFile(file);

        UserAnimalPhoto photo = fileStorageService.storeFile(file, description, userAnimal.getId());
        UserAnimalPhoto savedPhoto = userAnimalPhotoRepository.save(photo);
        userAnimal.addPhoto(savedPhoto);

        return userAnimalMapper.toDto(userAnimal);
    }

    public UserAnimalPhoto getPhoto(Long userId, Long photoId) {
        UserAnimalPhoto photo = findPhotoById(photoId);
        validatePhotoOwnership(photoId, userId);
        return photo;
    }

    public UserAnimalPhotoDto updatePhoto(Long userId, Long photoId, MultipartFile file, String newDescription) {
        log.info("Actualitzant foto {} de l'usuari {}", photoId, userId);

        UserAnimalPhoto photo = findPhotoById(photoId);
        validatePhotoOwnership(photoId, userId);

        if (file != null && !file.isEmpty()) {
            updatePhotoFile(photo, file);
        }

        if (newDescription != null) {
            photo.setDescription(newDescription);
        }

        UserAnimalPhoto updatedPhoto = userAnimalPhotoRepository.save(photo);
        log.info("Foto {} actualitzada correctament", photoId);

        return userAnimalMapper.toPhotoDto(updatedPhoto);
    }

    public void deletePhoto(Long userId, Long photoId) {
        log.info("Eliminant foto {} de l'usuari {}", photoId, userId);

        UserAnimalPhoto photo = findPhotoById(photoId);
        validatePhotoOwnership(photoId, userId);

        deletePhysicalFile(photo.getFileName());
        userAnimalPhotoRepository.delete(photo);

        log.info("Foto {} eliminada correctament", photoId);
    }

    private UserAnimal getUserAnimalWithValidation(Long userId, Long animalId) {
        UserAnimal userAnimal = userAnimalRepository.findByUserIdAndAnimalId(userId, animalId)
                .orElseThrow(() -> new UserAnimalNotFoundException("Aquest usuari no té aquest animal..."));

        if (userAnimal.getStatus() != AnimalStatus.UNLOCK) {
            throw new AnimalNotUnlockedException("Aquest animal no està desbloquejat...");
        }

        return userAnimal;
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null) {
            throw new InvalidFileException("La foto és obligatòria");
        }
        if (file.isEmpty()) {
            throw new InvalidFileException("La foto no pot estar buida");
        }
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new InvalidFileException("Només es permeten arxius d'imatge (JPEG, PNG, etc.)");
        }
    }

    private UserAnimalPhoto findPhotoById(Long photoId) {
        return userAnimalPhotoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));
    }

    private void validatePhotoOwnership(Long photoId, Long userId) {
        if (!userAnimalPhotoRepository.existsByIdAndUserId(photoId, userId)) {
            throw new UnauthorizedPhotoAccessException(photoId);
        }
    }

    private void updatePhotoFile(UserAnimalPhoto photo, MultipartFile file) {
        log.info("Actualitzant imatge de la foto {}", photo.getId());
        validateImageFile(file);

        deletePhysicalFile(photo.getFileName());

        UserAnimalPhoto updatedPhotoInfo = fileStorageService.storeFile(file, photo.getDescription(), photo.getUserAnimalId());
        photo.setFileName(updatedPhotoInfo.getFileName());
        photo.setOriginalFileName(updatedPhotoInfo.getOriginalFileName());
        photo.setContentType(updatedPhotoInfo.getContentType());
        photo.setFileSize(updatedPhotoInfo.getFileSize());
    }

    private void deletePhysicalFile(String fileName) {
        try {
            fileStorageService.deleteFile(fileName);
        } catch (Exception e) {
            log.warn("No s'ha pogut eliminar el fitxer físic: {}", fileName);
        }
    }
}