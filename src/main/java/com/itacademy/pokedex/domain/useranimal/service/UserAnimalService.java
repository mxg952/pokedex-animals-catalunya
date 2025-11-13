package com.itacademy.pokedex.domain.useranimal.service;

import com.itacademy.pokedex.domain.animal.exception.AnimalNotFoundException;
import com.itacademy.pokedex.domain.animal.mapper.AnimalMapper;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.useranimal.dto.UnlockAnimalRequest;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalDto;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalPhotoDto;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalStats;
import com.itacademy.pokedex.domain.useranimal.exception.*;
import com.itacademy.pokedex.domain.useranimal.mapper.UserAnimalMapper;
import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimal;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalPhotoRepository;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import io.jsonwebtoken.io.IOException;
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

    public UserAnimalService(AnimalRepository animalRepository, UserAnimalRepository userAnimalRepository, FileStorageService fileStorageService, AnimalMapper animalMapper, UserAnimalMapper userAnimalMapper, UserAnimalPhotoRepository userAnimalPhotoRepository) {
        this.animalRepository = animalRepository;
        this.userAnimalRepository = userAnimalRepository;
        this.fileStorageService = fileStorageService;
        this.userAnimalMapper = userAnimalMapper;
        this.userAnimalPhotoRepository = userAnimalPhotoRepository;
    }

    public UserAnimalDto unlockAnimal(Long userId, UnlockAnimalRequest request) {
        // Verificar animal existeix
        Animal animal = animalRepository.findByCommonName(request.getCommonName())
                .orElseThrow(() -> new AnimalNotFoundException("L'animal no s'ha trobat..."));

        // Verificar no està ja desbloquejat
        if (userAnimalRepository.findByUserIdAndAnimalId(userId, animal.getId()).isPresent()) {
            throw new AnimalAlreadyUnlockedException("L'animal ja està desbloquejat...");
        }

        // Crear UserAnimal
        UserAnimal userAnimal = UserAnimal.builder()
                .userId(userId)
                .animalId(animal.getId())
                .status(AnimalStatus.UNLOCK)
                .build();

        UserAnimal savedUserAnimal = userAnimalRepository.save(userAnimal);

        // Guardar foto real
        UserAnimalPhoto unlockPhoto = fileStorageService.storeFile(request.getFile(), request.getDescription(), savedUserAnimal.getId());
        UserAnimalPhoto savedPhoto = userAnimalPhotoRepository.save(unlockPhoto);
        savedUserAnimal.addPhoto(savedPhoto);

        Animal animalComplet = animalRepository.findById(animal.getId())
                .orElseThrow(() -> new AnimalNotFoundException("Animal no trobat"));

        // ✅ I passar animalComplet al mapper:
        return userAnimalMapper.toDto(savedUserAnimal, animalComplet);
    }

    public UserAnimalDto addPhoto(Long userId, Long animalId, MultipartFile file, String description) {
        // 1. Verificar que l'usuari té l'animal desbloquejat
        UserAnimal userAnimal = userAnimalRepository.findByUserIdAndAnimalId(userId, animalId)
                .orElseThrow(() -> new UserAnimalNotFoundException("Aquest usuari no té aquest animal..."));

        // 2. Verificar que està desbloquejat
        if (userAnimal.getStatus() != AnimalStatus.UNLOCK) {
            throw new AnimalNotUnlockedException("Aquest animal no està desbloquejat...");
        }

        // ✅ 3. VALIDAR que el fitxer no és null
        if (file == null) {
            throw new InvalidFileException("La foto és obligatòria");
        }

        // ✅ 4. VALIDAR que el fitxer no està buit
        if (file.isEmpty()) {
            throw new InvalidFileException("La foto no pot estar buida");
        }

        // ✅ 5. VALIDAR que és una imatge
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new InvalidFileException("Només es permeten arxius d'imatge (JPEG, PNG, etc.)");
        }

        // 6. Guardar la foto al sistema de fitxers
        UserAnimalPhoto photo = fileStorageService.storeFile(file, description, userAnimal.getId());

        // 7. Guardar la foto a la base de dades
        UserAnimalPhoto savedPhoto = userAnimalPhotoRepository.save(photo);

        // 8. Afegir la foto a la llista de l'usuari
        userAnimal.addPhoto(savedPhoto);

        // 9. Retornar DTO actualitzat
        return userAnimalMapper.toDto(userAnimal);
    }

    // Obtenir tots els animals desbloquejats d'un usuari amb fotos
    public List<UserAnimal> findUnlockedAnimalsWithPhotos(Long userId) {
        return userAnimalRepository.findUnlockedByUserIdWithPhotos(userId);
    }


    // Obtenir una foto específica amb verificació de propietari
    public UserAnimalPhoto getPhoto(Long userId, Long photoId) {
        UserAnimalPhoto photo = userAnimalPhotoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));

        // Verificar que l'usuari és el propietari
        if (!userAnimalPhotoRepository.existsByIdAndUserId(photoId, userId)) {
            throw new UnauthorizedPhotoAccessException(photoId);
        }

        return photo;
    }

    // Estadístiques de l'usuari
    public UserAnimalStats getStats(Long userId) {
        long totalUnlocked = userAnimalRepository.countByUserIdAndStatus(userId, AnimalStatus.UNLOCK);
        long totalPhotos = userAnimalPhotoRepository.countByUserId(userId);

        return UserAnimalStats.builder()
                .totalUnlockedAnimals(totalUnlocked)
                .totalPhotos(totalPhotos)
                .build();
    }

    public void deletePhoto(Long userId, Long photoId) {
        log.info("Eliminant foto {} de l'usuari {}", photoId, userId);

        // 1. Verificar que la foto existeix i l'usuari és propietari
        UserAnimalPhoto photo = userAnimalPhotoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));

        if (!userAnimalPhotoRepository.existsByIdAndUserId(photoId, userId)) {
            throw new UnauthorizedPhotoAccessException(photoId);
        }

        // 2. Eliminar fitxer físic
        try {
            fileStorageService.deleteFile(photo.getFileName());
        } catch (IOException e) {
            log.warn("No s'ha pogut eliminar el fitxer físic: {}", photo.getFileName());
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        // 3. Eliminar de la base de dades
        userAnimalPhotoRepository.delete(photo);

        log.info("Foto {} eliminada correctament", photoId);
    }

    // Obtenir les fotos d'un animal
    public List<UserAnimalPhoto> getAnimalPhotos(Long userId, Long animalId) {
        return userAnimalPhotoRepository.findByUserIdAndAnimalId(userId, animalId);
    }

    public List<UserAnimalDto> getUserAnimals(Long userId) {
        List<UserAnimal> userAnimals = userAnimalRepository.findByUserId(userId);

        return userAnimals.stream()
                .map(userAnimal -> {
                    // ✅ Carregar l'animal per a cada UserAnimal
                    Animal animal = animalRepository.findById(userAnimal.getAnimalId()).orElse(null);
                    return userAnimalMapper.toDto(userAnimal, animal);
                })
                .collect(Collectors.toList());
    }

    public UserAnimalPhotoDto updatePhoto(Long userId, Long photoId, MultipartFile file, String newDescription) {
        log.info("Actualitzant foto {} de l'usuari {}", photoId, userId);

        // 1. Verificar que la foto existeix i l'usuari és propietari
        UserAnimalPhoto photo = userAnimalPhotoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));

        if (!userAnimalPhotoRepository.existsByIdAndUserId(photoId, userId)) {
            throw new UnauthorizedPhotoAccessException(photoId);
        }

        // 2. Si s'ha pujat un nou fitxer, actualitzar-lo
        if (file != null && !file.isEmpty()) {
            log.info("Actualitzant imatge de la foto {}", photoId);

            // ✅ VALIDAR que és una imatge
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new InvalidFileException("Només es permeten arxius d'imatge (JPEG, PNG, etc.)");
            }

            // ✅ ELIMINAR fitxer antic
            try {
                fileStorageService.deleteFile(photo.getFileName());
            } catch (Exception e) {
                log.warn("No s'ha pogut eliminar el fitxer físic antic: {}", photo.getFileName());
            }

            // ✅ GUARDAR nou fitxer
            UserAnimalPhoto updatedPhotoInfo = fileStorageService.storeFile(file, newDescription, photo.getUserAnimalId());

            // ✅ ACTUALITZAR dades de la foto
            photo.setFileName(updatedPhotoInfo.getFileName());
            photo.setOriginalFileName(updatedPhotoInfo.getOriginalFileName());
            photo.setContentType(updatedPhotoInfo.getContentType());
            photo.setFileSize(updatedPhotoInfo.getFileSize());
        }

        // 3. Actualitzar descripció (si s'ha proporcionat)
        if (newDescription != null) {
            photo.setDescription(newDescription);
        }

        // 4. Guardar canvis
        UserAnimalPhoto updatedPhoto = userAnimalPhotoRepository.save(photo);
        log.info("Foto {} actualitzada correctament", photoId);

        // 5. Retornar DTO actualitzat
        return userAnimalMapper.toPhotoDto(updatedPhoto);
    }
}
