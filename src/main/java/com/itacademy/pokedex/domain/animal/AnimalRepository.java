package com.itacademy.pokedex.domain.animal;

import com.itacademy.pokedex.domain.useranimal.UserAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByCommonName(String nomColoquial);
    List<UserAnimal> findByUserId(Long userId);


}
