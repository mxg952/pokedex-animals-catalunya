package com.itacademy.pokedex.domain.animal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UnlockAnimalRequest {

    @NotNull
    @Size(min = 2, max = 50, message = "Nom del animal invàlid")
    private String commonName;
    private MultipartFile photoUrl;

}
