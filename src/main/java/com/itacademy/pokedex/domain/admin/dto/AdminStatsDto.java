package com.itacademy.pokedex.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDto {

    private Long totalUsers;
    private Long totalUnlockedAnimals;
    private Long totalPhotos;
    private String generatedAt;
}
