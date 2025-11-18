package com.itacademy.pokedex.domain.animal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/images")
public class AnimalImageController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping("/animals/{type}/{filename}")
    public ResponseEntity<Resource> getAnimalImage( @PathVariable String type, @PathVariable String filename) {
        try {
            String imagePath = uploadDir + File.separator + "animals" + File.separator + type + File.separator + filename;
            File imageFile = new File(imagePath);

            if (imageFile.exists() && imageFile.isFile()) {
                Resource resource = new FileSystemResource(imageFile);

                MediaType mediaType = determineMediaType(filename);

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header("Cache-Control", "max-age=3600")
                        .body(resource);
            } else {
                System.out.println("Imatge no trobada: " + imagePath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("Error servint imatge: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user-animals/{filename}")
    public ResponseEntity<Resource> getUserAnimalImage(@PathVariable String filename) {
        try {
            String imagePath = uploadDir + File.separator + filename;
            File imageFile = new File(imagePath);

            if (imageFile.exists() && imageFile.isFile()) {
                Resource resource = new FileSystemResource(imageFile);
                MediaType mediaType = determineMediaType(filename);

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header("Cache-Control", "max-age=3600")
                        .body(resource);
            } else {
                System.out.println("Imatge d'usuari no trobada: " + imagePath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("Error servint imatge d'usuari: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private MediaType determineMediaType(String filename) {
        String lowerCaseFilename = filename.toLowerCase();

        if (lowerCaseFilename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (lowerCaseFilename.endsWith(".jpg") || lowerCaseFilename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (lowerCaseFilename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (lowerCaseFilename.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        } else {
            return MediaType.IMAGE_JPEG;
        }
    }
}