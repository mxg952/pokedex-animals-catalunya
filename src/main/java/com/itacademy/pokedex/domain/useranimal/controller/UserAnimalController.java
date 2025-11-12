package com.itacademy.pokedex.domain.useranimal.controller;


import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.useranimal.dto.UnlockAnimalRequest;
import com.itacademy.pokedex.domain.useranimal.dto.UpdatePhotoRequest;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalDto;
import com.itacademy.pokedex.domain.useranimal.dto.UserAnimalPhotoDto;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimal;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import com.itacademy.pokedex.domain.useranimal.service.FileStorageService;
import com.itacademy.pokedex.domain.useranimal.service.UserAnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-animals")
public class UserAnimalController {

    private final UserAnimalService userAnimalService;
    private final FileStorageService fileStorageService;

 @PostMapping(value = "/unlock", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserAnimalDto> unlockAnimal(@Valid @ModelAttribute UnlockAnimalRequest unlockAnimalRequest,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
     Long userId = ((User) userDetails).getId();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userAnimalService.unlockAnimal(userId, unlockAnimalRequest));
 }


    @GetMapping("/get")
    public ResponseEntity<List<UserAnimalDto>> getUserAnimals(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userAnimalService.getUserAnimals(userId));
    }

    @PostMapping(value = "/{animalId}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserAnimalDto> addPhoto(
            @PathVariable Long animalId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @AuthenticationPrincipal User user) {

        UserAnimalDto result = userAnimalService.addPhoto(user.getId(), animalId, file, description);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{photoId}/download")
    public ResponseEntity<Resource> downloadPhoto(
            @PathVariable Long photoId,
            @AuthenticationPrincipal User user) {

        UserAnimalPhoto photo = userAnimalService.getPhoto(user.getId(), photoId);

        // Ara fem servir photo.getFileName() directament
        Resource file = fileStorageService.loadFile(photo.getFileName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(photo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + photo.getOriginalFileName() + "\"")
                .body(file);
    }

    @DeleteMapping("/photos/{photoId}")
    public ResponseEntity<Map<String, String>> deletePhoto(
            @PathVariable Long photoId,
            @AuthenticationPrincipal User user) {

        userAnimalService.deletePhoto(user.getId(), photoId);
        return ResponseEntity.ok(Map.of(
                "message", "Foto eliminada correctament",
                "success", "true"
        ));
    }

    @PutMapping("/photos/{photoId}")
    public ResponseEntity<UserAnimalPhotoDto> updatePhoto(
            @PathVariable Long photoId,
            @RequestBody UpdatePhotoRequest request,
            @AuthenticationPrincipal User user) {

        UserAnimalPhotoDto updatedPhoto = userAnimalService.updatePhoto(
                user.getId(), photoId, request.getDescription());
        return ResponseEntity.ok(updatedPhoto);
    }
}
