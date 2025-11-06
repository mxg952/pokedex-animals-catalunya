package com.itacademy.pokedex.domain.user.dto;

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
public class LoginRequest {

    @NotNull
    @Size(min = 2, max = 50, message = "Nom invàlid...")
    private String name;

    @NotNull
    @Size(min = 5, message = "La contrasenya és massa curta...")
    private String password;

}
