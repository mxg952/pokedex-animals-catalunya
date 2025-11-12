package com.itacademy.pokedex.domain.animal.controller;

import com.itacademy.pokedex.domain.animal.dto.AnimalDto;
import com.itacademy.pokedex.domain.animal.dto.AnimalLockDto;
import com.itacademy.pokedex.domain.animal.mapper.AnimalMapper;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.service.AnimalService;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;
    private final AnimalMapper animalMapper;  // ✅ INJECTA el mapper

    @GetMapping("/get")
    public ResponseEntity<List<AnimalLockDto>> getAllAnimals() {  // ✅ Retorna DTO
        List<Animal> animals = animalService.getAllAnimals();

        // ✅ TRANSFORMA entitats a DTOs amb URLs completes
        List<AnimalLockDto> animalDtos = animals.stream()
                .map(animalMapper::toLockDto)
                .collect(Collectors.toList());

        log.info("🔍 Retornant {} animals amb URLs d'imatges", animalDtos.size());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(animalDtos);
    }

    // ✅ AFEGEIX endpoint per animal desbloquejat
    @GetMapping("/{id}/unlocked")
    public ResponseEntity<?> getUnlockedAnimal(@PathVariable Long id) {
        try {
            Animal animal = animalService.getAnimalById(id);
            // ✅ Transforma a DTO amb URL desbloquejada
            return ResponseEntity.ok(animalMapper.toUnlockDto(animal));
        } catch (Exception e) {
            log.error("Error obtenint animal desbloquejat: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<
            AnimalDto>> searchAnimals(
            @RequestParam String name,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(animalService.searchAnimal(name, userId));
    }
}