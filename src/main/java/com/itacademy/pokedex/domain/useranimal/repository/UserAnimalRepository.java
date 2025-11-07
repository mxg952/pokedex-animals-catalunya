package com.itacademy.pokedex.domain.useranimal.repository;

import com.itacademy.pokedex.domain.useranimal.modelo.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.modelo.entity.UserAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAnimalRepository extends JpaRepository<UserAnimal, Long> {
    Optional<UserAnimal> findByUserIdAndAnimalId(Long userId, Long animalId);
    boolean existsByUserIdAndAnimalIdAndStatus(Long userId, Long animalId, AnimalStatus status);
}
