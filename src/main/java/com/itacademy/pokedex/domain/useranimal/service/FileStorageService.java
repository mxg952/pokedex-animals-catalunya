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
import java.nio.file.Paths;

@Slf4j
@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            log.info("Directori d'uploads creat: {}", uploadDir);
        } catch (IOException e) {
            log.error("No s'ha pogut crear el directori d'uploads: {}", uploadDir, e);
        }
    }

    public UserAnimalPhoto storeFile(MultipartFile file, String description, Long userAnimalId) {
        try {
            // Crear directori si no existeix
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Nom únic simple
            String originalName = file.getOriginalFilename();
            assert originalName != null;
            String fileExtension = originalName.substring(originalName.lastIndexOf("."));
            String fileName = System.currentTimeMillis() + "_" + userAnimalId + fileExtension;

            // Guardar arxiu
            File destination = new File(directory, fileName);
            file.transferTo(destination);

            // Crear entitat
            return UserAnimalPhoto.builder()
                    .fileName(fileName)
                    .originalFileName(originalName)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .description(description)
                    .userAnimalId(userAnimalId)
                    .build();

        } catch (Exception ex) {
            throw new RuntimeException("Error guardant fitxer: " + ex.getMessage());
        }
    }

    public FileSystemResource loadFile(String fileName) {
        try {
            File file = new File(uploadDir, fileName);
            return new FileSystemResource(file);
        } catch (Exception ex) {
            throw new RuntimeException("Error carregant fitxer: " + fileName);
        }
    }

    public void deleteFile(String fileName) {
        try {
            File file = new File(uploadDir, fileName);
            file.delete();
        } catch (Exception ex) {
            throw new RuntimeException("Error eliminant fitxer: " + fileName);
        }
    }
}
