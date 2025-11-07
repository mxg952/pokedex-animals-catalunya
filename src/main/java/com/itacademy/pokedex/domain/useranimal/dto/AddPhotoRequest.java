package com.itacademy.pokedex.domain.useranimal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPhotoRequest {
    private String photoUrl;
    private String description;
}
