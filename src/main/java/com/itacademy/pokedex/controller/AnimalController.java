package com.itacademy.pokedex.controller;

import com.itacademy.pokedex.model.dto.request.UnlockAnimalRequest;
import com.itacademy.pokedex.model.entity.Animal;
import com.itacademy.pokedex.model.entity.UserAnimal;
import com.itacademy.pokedex.service.AnimalService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/animals")
public class AnimalController {

 private final AnimalService animalService;

 /*
 @PostMapping("/user-animals/unlock")
    public ResponseEntity<UserAnimal> unlockAnimal(@Valid @RequestBody UnlockAnimalRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
     Long userId = userDetails.getId();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(animalService.unlockAnimal(userId,request));
 }*/

    /*
    @GetMapping("user-animals")
    public ResponseEntity<List<UserAnimal>> getUserAnimals(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(animalService.getUserAnimals(userDetails.getId()));
    }*/

    @GetMapping("/animals")
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return  ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(animalService.getAllAnimals());
    }

}
