package com.itacademy.pokedex.domain.useranimal.repository;

import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnimalPhotoRepository extends JpaRepository<UserAnimalPhoto, Long> {

    // Trobar totes les fotos d'una relació user-animal
    List<UserAnimalPhoto> findByUserAnimalId(Long userAnimalId);

    // Trobar fotos per usuari
    @Query("SELECT p FROM UserAnimalPhoto p JOIN UserAnimal ua ON p.userAnimalId = ua.id WHERE ua.userId = :userId")
    List<UserAnimalPhoto> findByUserId(@Param("userId") Long userId);

    // Trobar fotos per animal i usuari
    @Query("SELECT p FROM UserAnimalPhoto p JOIN UserAnimal ua ON p.userAnimalId = ua.id WHERE ua.userId = :userId AND ua.animalId = :animalId")
    List<UserAnimalPhoto> findByUserIdAndAnimalId(@Param("userId") Long userId, @Param("animalId") Long animalId);

    // Trobar la primera foto d'un animal (foto de desbloqueig)
    @Query("SELECT p FROM UserAnimalPhoto p JOIN UserAnimal ua ON p.userAnimalId = ua.id WHERE ua.userId = :userId AND ua.animalId = :animalId ORDER BY p.uploadedAt ASC LIMIT 1")
    Optional<UserAnimalPhoto> findFirstByUserIdAndAnimalId(@Param("userId") Long userId, @Param("animalId") Long animalId);

    // Trobar les últimes fotos pujades per usuari
    @Query("SELECT p FROM UserAnimalPhoto p JOIN UserAnimal ua ON p.userAnimalId = ua.id WHERE ua.userId = :userId ORDER BY p.uploadedAt DESC")
    List<UserAnimalPhoto> findRecentByUserId(@Param("userId") Long userId, org.springframework.data.domain.Pageable pageable);

    // Comptar fotos d'una relació user-animal
    long countByUserAnimalId(Long userAnimalId);

    // Comptar fotos per usuari
    @Query("SELECT COUNT(p) FROM UserAnimalPhoto p JOIN UserAnimal ua ON p.userAnimalId = ua.id WHERE ua.userId = :userId")
    long countByUserId(@Param("userId") Long userId);

    // Verificar si una foto pertany a un usuari
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM UserAnimalPhoto p JOIN UserAnimal ua ON p.userAnimalId = ua.id WHERE p.id = :photoId AND ua.userId = :userId")
    boolean existsByIdAndUserId(@Param("photoId") Long photoId, @Param("userId") Long userId);

    // Eliminar fotos per relació user-animal
    void deleteByUserAnimalId(Long userAnimalId);

    // Eliminar totes les fotos d'un usuari
    @Query("DELETE FROM UserAnimalPhoto p WHERE p.userAnimalId IN (SELECT ua.id FROM UserAnimal ua WHERE ua.userId = :userId)")
    void deleteByUserId(@Param("userId") Long userId);
}
