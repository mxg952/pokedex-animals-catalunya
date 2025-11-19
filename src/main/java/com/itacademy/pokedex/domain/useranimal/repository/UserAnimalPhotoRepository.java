package com.itacademy.pokedex.domain.useranimal.repository;

import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnimalPhotoRepository extends JpaRepository<UserAnimalPhoto, Long> {
    @Query("SELECT COUNT(p) FROM UserAnimalPhoto p JOIN UserAnimal ua ON p.userAnimalId = ua.id WHERE ua.userId = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM UserAnimalPhoto p JOIN UserAnimal ua ON p.userAnimalId = ua.id WHERE p.id = :photoId AND ua.userId = :userId")
    boolean existsByIdAndUserId(@Param("photoId") Long photoId, @Param("userId") Long userId);

}
