package com.itacademy.pokedex.domain.animal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class AnimalImageController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping("/animals/{type}/{filename}")
    public ResponseEntity<Resource> getAnimalImage(
            @PathVariable String type,
            @PathVariable String filename) {

        try {
            // Ruta completa: uploads/animals/locked/o/unlocked/filename
            String imagePath = uploadDir + File.separator + "animals" + File.separator + type + File.separator + filename;
            File imageFile = new File(imagePath);

            if (imageFile.exists() && imageFile.isFile()) {
                Resource resource = new FileSystemResource(imageFile);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}