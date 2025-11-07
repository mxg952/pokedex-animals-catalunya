package com.itacademy.pokedex.domain.animal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnimalRequest {

    @NotNull
    @Size(min = 2, max = 50, message = "Nom invàlid...")
    private String animalNme;
}
