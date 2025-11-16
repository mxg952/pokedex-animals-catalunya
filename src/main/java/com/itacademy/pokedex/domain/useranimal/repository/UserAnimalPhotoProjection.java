package com.itacademy.pokedex.domain.useranimal.repository;

import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAnimalPhotoProjection {
    private UserAnimalPhoto photo;
    private User user;
    private Animal animal;

    // Métodos helper para acceder fácilmente a los datos
    public Long getPhotoId() {
        return photo != null ? photo.getId() : null;
    }

    public String getUserName() {
        return user != null ? user.getName() : null;
    }

    public String getAnimalName() {
        return animal != null ? animal.getCommonName() : null;
    }

    public String getPhotoStatus() {
        return photo != null ? photo.getStatus() : null;
    }
}