package com.itacademy.pokedex.domain.useranimal.service;

import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileStorageService {

    @Value("${app.upload.dir:C:/Users/marcg/Desktop/pokedex-animals-catalunya/uploads}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            log.info("Inicialitzant FileStorageService...");
            log.info("uploadDir: {}", uploadDir);

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Path animalsPath = uploadPath.resolve("animals");
            Path lockedPath = animalsPath.resolve("locked");
            Path unlockedPath = animalsPath.resolve("unlocked");

            Files.createDirectories(lockedPath);
            Files.createDirectories(unlockedPath);

            log.info("Directoris creats correctament:");
            log.info("- Upload: {}", uploadPath);
            log.info("- Animals: {}", animalsPath);
            log.info("- Locked: {}", lockedPath);
            log.info("- Unlocked: {}", unlockedPath);

        } catch (IOException e) {
            log.error("Error creant directoris: {}", uploadDir, e);
        }
    }

    public UserAnimalPhoto storeFile(MultipartFile file, String description, Long userAnimalId) {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);

            String originalName = file.getOriginalFilename();
            assert originalName != null;
            String fileExtension = originalName.substring(originalName.lastIndexOf("."));
            String fileName = System.currentTimeMillis() + "_" + userAnimalId + fileExtension;

            File destination = new File(uploadPath.toFile(), fileName);
            file.transferTo(destination);

            return UserAnimalPhoto.builder()
                    .fileName(fileName)
                    .originalFileName(originalName)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .description(description)
                    .userAnimalId(userAnimalId)
                    .uploadedAt(java.time.LocalDateTime.now())
                    .build();

        } catch (Exception ex) {
            throw new RuntimeException("Error guardant fitxer: " + ex.getMessage());
        }
    }

    public FileSystemResource loadFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().resolve(fileName);
            File file = filePath.toFile();

            if (file.exists()) {
                return new FileSystemResource(file);
            } else {
                throw new RuntimeException("Fitxer no trobat: " + fileName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error carregant fitxer: " + fileName);
        }
    }

    public void deleteFile(String fileName) throws IOException {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().resolve(fileName);

            log.info("Intentant eliminar fitxer: {}", filePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Fitxer eliminat correctament: {}", fileName);
            } else {
                log.warn("El fitxer no existeix, no es pot eliminar: {}", fileName);
            }
        } catch (IOException e) {
            log.error("Error eliminant fitxer: {}", fileName, e);
            throw new IOException("No s'ha pogut eliminar el fitxer: " + fileName, e);
        } catch (Exception e) {
            log.error("Error inesperat eliminant fitxer: {}", fileName, e);
            throw new RuntimeException("Error eliminant fitxer: " + fileName, e);
        }
    }

    public void deleteAnimalImage(String type, String filename) {
        try {
            if (isValidImageType(type)) {
                log.error("Tipus d'imatge invalid: {}", type);
                return;
            }

            Path filePath = buildAnimalImagePath(type, filename);

            log.info("Intentant eliminar imatge d'animal: {}", filePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Imatge d'animal eliminada: {}/{}", type, filename);
            } else {
                log.warn("La imatge d'animal no existeix: {}/{}", type, filename);
            }

        } catch (IOException e) {
            log.error("Error eliminant imatge d'animal: {}/{}", type, filename, e);
        } catch (Exception e) {
            log.error("Error inesperat eliminant imatge d'animal: {}/{}", type, filename, e);
        }
    }

    public String storeAnimalImage(MultipartFile file, String type) {
        try {
            if (isValidImageType(type)) {
                log.error("Tipus d'imatge no valid: {}", type);
                throw new IllegalArgumentException("Tipus d'imatge no valid: " + type);
            }

            Path animalsPath = buildAnimalImagePath(type, "");
            Files.createDirectories(animalsPath);

            String originalName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalName != null && originalName.contains(".")) {
                fileExtension = originalName.substring(originalName.lastIndexOf("."));
            }

            String fileName = System.currentTimeMillis() + "_" +
                    Math.abs(java.util.UUID.randomUUID().hashCode()) + fileExtension;

            Path filePath = animalsPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            log.info("Imatge d'animal guardada: {}/{}", type, fileName);
            return fileName;

        } catch (Exception ex) {
            log.error("Error guardant imatge d'animal: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error guardant imatge d'animal: " + ex.getMessage(), ex);
        }
    }

    private Path buildAnimalImagePath(String type, String filename) {
        return Paths.get(uploadDir).toAbsolutePath()
                .resolve("animals")
                .resolve(type)
                .resolve(filename);
    }

    private boolean isValidImageType(String type) {
        return !type.equals("locked") && !type.equals("unlocked");
    }
}