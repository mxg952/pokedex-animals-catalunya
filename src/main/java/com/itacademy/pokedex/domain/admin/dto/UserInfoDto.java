package com.itacademy.pokedex.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private Long unlockedAnimals;
    private Long uploadedPhotos;
}
