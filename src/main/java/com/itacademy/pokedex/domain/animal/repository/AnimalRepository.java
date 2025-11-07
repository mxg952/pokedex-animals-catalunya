package com.itacademy.pokedex.domain.animal.repository;

import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByCommonName(String commonName);
    List<UserAnimal> findByUserId(Long userId);
    List<Animal> findByCommonNameContainingIgnoreCase(String commonName);


}
