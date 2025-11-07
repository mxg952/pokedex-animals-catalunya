package com.itacademy.pokedex.domain.useranimal.controller;


import com.itacademy.pokedex.domain.animal.service.AnimalService;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.useranimal.dto.UnlockRequest;
import com.itacademy.pokedex.domain.useranimal.modelo.entity.UserAnimal;
import com.itacademy.pokedex.domain.useranimal.service.UserAnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.swing.event.AncestorEvent;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-animals")
public class UserAnimalController {

    private UserAnimalService UserAnimalService;

 @PostMapping("/unlock")
    public ResponseEntity<UserAnimal> unlockAnimal(@Valid @RequestBody UnlockRequest unlockRequest,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
     Long userId = ((User) userDetails).getId();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(UserAnimalService.unlockAnimal(userId,unlockRequest));
 }


    @GetMapping("/get")
    public ResponseEntity<List<UserAnimal>> getUserAnimals(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(UserAnimalService.getUserAnimals(userId));
    }
}
