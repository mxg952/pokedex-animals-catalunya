package com.itacademy.pokedex.domain.admin.controller;

import com.itacademy.pokedex.domain.admin.dto.AdminStatsDto;
import com.itacademy.pokedex.domain.admin.dto.CreateAnimalRequest;
import com.itacademy.pokedex.domain.admin.dto.UserInfoDto;
import com.itacademy.pokedex.domain.admin.service.AdminService;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN_ROLE')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserInfoDto>> getAllUsers() {
        List<UserInfoDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/animals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Animal> createAnimal(
            @Valid @ModelAttribute CreateAnimalRequest request) {
        Animal createdAnimal = adminService.createAnimal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnimal);
    }
}
