package com.itacademy.pokedex.domain.useranimal.service;

import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileStorageService {

    // ✅ VALORS PER DEFECTE per si les properties no existeixen
    @Value("${app.upload.dir:C:/Users/marcg/Desktop/pokedex-animals-catalunya/uploads}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            log.info("🔄 Inicialitzant FileStorageService...");
            log.info("📁 uploadDir: {}", uploadDir);

            // ✅ Crear directoris amb valors absoluts
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Path animalsPath = uploadPath.resolve("animals");
            Path lockedPath = animalsPath.resolve("locked");
            Path unlockedPath = animalsPath.resolve("unlocked");

            // Crear tots els directoris
            Files.createDirectories(lockedPath);
            Files.createDirectories(unlockedPath);

            log.info("✅ Directoris creats correctament:");
            log.info("✅ - Upload: {}", uploadPath);
            log.info("✅ - Animals: {}", animalsPath);
            log.info("✅ - Locked: {}", lockedPath);
            log.info("✅ - Unlocked: {}", unlockedPath);

        } catch (IOException e) {
            log.error("❌ Error creant directoris: {}", uploadDir, e);
            // ✅ NO llençar excepció, només log error
        }
    }

    // Mètode per carregar imatges d'animals
    public Resource loadAnimalImage(String type, String filename) {
        try {
            // ✅ Ruta absoluta
            Path filePath = Paths.get(uploadDir).toAbsolutePath()
                    .resolve("animals")
                    .resolve(type)
                    .resolve(filename);

            File file = filePath.toFile();

            if (file.exists() && file.isFile()) {
                log.debug("✅ Imatge trobada: {}", filePath);
                return new FileSystemResource(file);
            } else {
                log.warn("❌ Imatge no trobada: {}", filePath);
                return null;
            }
        } catch (Exception ex) {
            log.error("❌ Error carregant imatge: {}/{}", type, filename, ex);
            return null;
        }
    }

    // Els teus mètodes existents...
    public UserAnimalPhoto storeFile(MultipartFile file, String description, Long userAnimalId) {
        try {
            // ✅ Assegurar que el directori existeix
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);

            // ... el teu codi actual
            String originalName = file.getOriginalFilename();
            String fileExtension = originalName.substring(originalName.lastIndexOf("."));
            String fileName = System.currentTimeMillis() + "_" + userAnimalId + fileExtension;

            // Guardar arxiu
            File destination = new File(uploadPath.toFile(), fileName);
            file.transferTo(destination);

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
            // ✅ Ruta absoluta
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

    public void deleteFile(String fileName) throws java.io.IOException {
        try {
            // ✅ Ruta absoluta del fitxer
            Path filePath = Paths.get(uploadDir).toAbsolutePath().resolve(fileName);

            log.info("🗑️ Intentant eliminar fitxer: {}", filePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("✅ Fitxer eliminat correctament: {}", fileName);
            } else {
                log.warn("⚠️ El fitxer no existeix, no es pot eliminar: {}", fileName);
                // No llençar excepció, només log warning
            }

        } catch (java.io.IOException e) {
            log.error("❌ Error eliminant fitxer: {}", fileName, e);
            throw new java.io.IOException("No s'ha pogut eliminar el fitxer: " + fileName, e);
        } catch (Exception e) {
            log.error("❌ Error inesperat eliminant fitxer: {}", fileName, e);
            throw new RuntimeException("Error eliminant fitxer: " + fileName, e);
        }
    }

    /**
     * ✅ ELIMINA una imatge d'animal (locked o unlocked)
     */
    public boolean deleteAnimalImage(String type, String filename) {
        try {
            // Validar tipus
            if (!type.equals("locked") && !type.equals("unlocked")) {
                log.error("❌ Tipus d'imatge no vàlid: {}", type);
                return false;
            }

            // ✅ Ruta de la imatge d'animal
            Path filePath = Paths.get(uploadDir).toAbsolutePath()
                    .resolve("animals")
                    .resolve(type)
                    .resolve(filename);

            log.info("🗑️ Intentant eliminar imatge d'animal: {}", filePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("✅ Imatge d'animal eliminada: {}/{}", type, filename);
                return true;
            } else {
                log.warn("⚠️ La imatge d'animal no existeix: {}/{}", type, filename);
                return false;
            }

        } catch (java.io.IOException e) {
            log.error("❌ Error eliminant imatge d'animal: {}/{}", type, filename, e);
            return false;
        } catch (Exception e) {
            log.error("❌ Error inesperat eliminant imatge d'animal: {}/{}", type, filename, e);
            return false;
        }
    }

    /**
     * ✅ VERIFICA si un fitxer existeix
     */
    public boolean fileExists(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().resolve(fileName);
            return Files.exists(filePath) && Files.isRegularFile(filePath);
        } catch (Exception e) {
            log.error("❌ Error verificant existència del fitxer: {}", fileName, e);
            return false;
        }
    }

    /**
     * ✅ OBTÉ la mida d'un fitxer
     */
    public long getFileSize(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().resolve(fileName);
            if (Files.exists(filePath)) {
                return Files.size(filePath);
            }
            return 0L;
        } catch (Exception e) {
            log.error("❌ Error obtenint mida del fitxer: {}", fileName, e);
            return 0L;
        }
    }

}
