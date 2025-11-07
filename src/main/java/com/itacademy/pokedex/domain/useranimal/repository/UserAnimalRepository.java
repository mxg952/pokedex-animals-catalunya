package com.itacademy.pokedex.domain.useranimal.repository;

import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnimalRepository extends JpaRepository<UserAnimal, Long> {
    Optional<UserAnimal> findByUserIdAndAnimalId(Long userId, Long animalId);
    boolean existsByUserIdAndAnimalIdAndStatus(Long userId, Long animalId, AnimalStatus status);
    // Trobar tots els animals d'un usuari
    List<UserAnimal> findByUserId(Long userId);

    // Trobar animals d'un usuari per estat
    List<UserAnimal> findByUserIdAndStatus(Long userId, AnimalStatus status);

    // Trobar animals desbloquejats d'un usuari
    List<UserAnimal> findByUserIdAndStatusOrderByUnlockedAtDesc(Long userId, AnimalStatus status);

    // Comprovar si un usuari té un animal desbloquejat

    // Comprovar si un usuari té un animal (qualsevol estat)
    boolean existsByUserIdAndAnimalId(Long userId, Long animalId);

    // Trobar animals per usuari amb les fotos carregades (eager loading)
    @Query("SELECT ua FROM UserAnimal ua LEFT JOIN FETCH ua.photos WHERE ua.userId = :userId")
    List<UserAnimal> findByUserIdWithPhotos(@Param("userId") Long userId);

    // Trobar animals desbloquejats amb les fotos carregades
    @Query("SELECT ua FROM UserAnimal ua LEFT JOIN FETCH ua.photos WHERE ua.userId = :userId AND ua.status = 'UNLOCK'")
    List<UserAnimal> findUnlockedByUserIdWithPhotos(@Param("userId") Long userId);

    // Trobar un animal específic d'un usuari amb les fotos
    @Query("SELECT ua FROM UserAnimal ua LEFT JOIN FETCH ua.photos WHERE ua.userId = :userId AND ua.animalId = :animalId")
    Optional<UserAnimal> findByUserIdAndAnimalIdWithPhotos(@Param("userId") Long userId, @Param("animalId") Long animalId);

    // Comptar animals desbloquejats per usuari
    long countByUserIdAndStatus(Long userId, AnimalStatus status);

    // Comptar total de fotos d'un usuari
    @Query("SELECT COUNT(p) FROM UserAnimal ua JOIN ua.photos p WHERE ua.userId = :userId")
    long countTotalPhotosByUserId(@Param("userId") Long userId);

    // Trobar els últims animals desbloquejats (per dashboard)
    List<UserAnimal> findTop5ByUserIdAndStatusOrderByUnlockedAtDesc(Long userId, AnimalStatus status);

    // Trobar animals per categoria
    @Query("SELECT ua FROM UserAnimal ua JOIN ua.animal a WHERE ua.userId = :userId AND a.category = :category")
    List<UserAnimal> findByUserIdAndAnimalCategory(@Param("userId") Long userId, @Param("category") String category);

    // Eliminar per usuari i animal
    void deleteByUserIdAndAnimalId(Long userId, Long animalId);

    // Eliminar tots els animals d'un usuari
    void deleteByUserId(Long userId);
}
