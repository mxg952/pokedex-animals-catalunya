package com.itacademy.pokedex.domain.useranimal.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-animals")
public class UserAnimalController {
    /*
 @PostMapping("/unlock")
    public ResponseEntity<UserAnimal> unlockAnimal(@Valid @RequestBody UnlockAnimalRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
     Long userId = userDetails.getId();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(animalService.unlockAnimal(userId,request));
 }*/

    /*
    @GetMapping
    public ResponseEntity<List<UserAnimal>> getUserAnimals(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(animalService.getUserAnimals(userDetails.getId()));
    }*/
}
