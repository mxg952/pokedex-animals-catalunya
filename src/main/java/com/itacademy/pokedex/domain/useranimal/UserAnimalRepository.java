package com.itacademy.pokedex.domain.useranimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAnimalRepository extends JpaRepository<UserAnimal, Long> {
    Optional<UserAnimal> findByUserIdAndAnimalId(Long userId, Long animalId);
}
