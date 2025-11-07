package com.itacademy.pokedex.domain.animal.controller;

import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.service.AnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/animals")
public class AnimalController {

 private final AnimalService animalService;

    @GetMapping("/get")
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return  ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(animalService.getAllAnimals());
    }

}
