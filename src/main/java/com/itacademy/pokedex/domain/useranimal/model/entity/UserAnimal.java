package com.itacademy.pokedex.domain.useranimal.model.entity;

import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user_animals")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "animal_id", nullable = false)
    private Long animalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AnimalStatus status = AnimalStatus.LOCK;

    @Column(name = "custom_photo_url")
    private String customPhotoUrl;

    @Column(name = "unlocked_at")
    private LocalDateTime unlockedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relació OneToMany amb les fotos de l'usuari
    @OneToMany(mappedBy = "userAnimalId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserAnimalPhoto> photos = new ArrayList<>();

    // Relació ManyToOne amb l'entitat Animal (read-only)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", insertable = false, updatable = false)
    private Animal animal;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        // Si es crea com a UNLOCK, establir la data de desbloqueig
        if (status == AnimalStatus.UNLOCK && unlockedAt == null) {
            unlockedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Si canvia de LOCK a UNLOCK, establir la data de desbloqueig
        if (status == AnimalStatus.UNLOCK && unlockedAt == null) {
            unlockedAt = LocalDateTime.now();
        }
    }

    public void unlock() {
        this.status = AnimalStatus.UNLOCK;
        if (this.unlockedAt == null) {
            this.unlockedAt = LocalDateTime.now();
        }
    }

    public void lock() {
        if (hasPhotos()) {
            throw new IllegalStateException("No es pot bloquejar un animal amb fotos");
        }
        this.status = AnimalStatus.LOCK;
    }

    public void addPhoto(UserAnimalPhoto photo) {
        if (photos == null) {
            photos = new ArrayList<>();
        }

        // Assegurar que la foto té la referència correcta
        photo.setUserAnimalId(this.id);
        photos.add(photo);
    }

    public void removePhoto(UserAnimalPhoto photo) {
        if (photos != null) {
            photos.remove(photo);
        }
    }

    public boolean removePhotoById(Long photoId) {
        if (photos != null) {
            return photos.removeIf(photo -> photo.getId().equals(photoId));
        }
        return false;
    }

    public boolean isUnlocked() {
        return status == AnimalStatus.UNLOCK;
    }

    public boolean isLocked() {
        return status == AnimalStatus.LOCK;
    }

    public boolean hasPhotos() {
        return photos != null && !photos.isEmpty();
    }

    public int getPhotoCount() {
        return photos != null ? photos.size() : 0;
    }

    public UserAnimalPhoto getFirstPhoto() {
        if (hasPhotos()) {
            return photos.get(0);
        }
        return null;
    }


    public String getMainPhotoUrl() {
        if (hasPhotos()) {
            return getFirstPhoto().getFileName();
        }
        if (animal != null && animal.getPhotoUnlockUrl() != null) {
            return animal.getPhotoUnlockUrl();
        }
        return customPhotoUrl;
    }

    public String getAnimalName() {
        return animal != null ? animal.getCommonName() : "Animal " + animalId;
    }

    public String getAnimalCategory() {
        return animal != null ? animal.getCategory() : null;
    }

    public Long getDaysSinceUnlock() {
        if (unlockedAt == null) {
            return null;
        }
        return java.time.Duration.between(unlockedAt, LocalDateTime.now()).toDays();
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public static UserAnimal createUnlocked(Long userId, Long animalId) {
        return UserAnimal.builder()
                .userId(userId)
                .animalId(animalId)
                .status(AnimalStatus.UNLOCK)
                .unlockedAt(LocalDateTime.now())
                .build();
    }

    public static UserAnimal createLocked(Long userId, Long animalId) {
        return UserAnimal.builder()
                .userId(userId)
                .animalId(animalId)
                .status(AnimalStatus.LOCK)
                .build();
    }

    @Override
    public String toString() {
        return "UserAnimal{" +
                "id=" + id +
                ", userId=" + userId +
                ", animalId=" + animalId +
                ", status=" + status +
                ", photoCount=" + getPhotoCount() +
                ", unlockedAt=" + unlockedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAnimal that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
