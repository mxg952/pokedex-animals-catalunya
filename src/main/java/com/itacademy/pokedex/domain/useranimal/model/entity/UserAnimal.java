package com.itacademy.pokedex.domain.useranimal.model.entity;

import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "userAnimalId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserAnimalPhoto> photos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", insertable = false, updatable = false)
    private Animal animal;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == AnimalStatus.UNLOCK && unlockedAt == null) {
            unlockedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

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

    public void addPhoto(UserAnimalPhoto photo) {
        if (photos == null) {
            photos = new ArrayList<>();
        }

        photo.setUserAnimalId(this.id);
        photos.add(photo);
    }

    public boolean hasPhotos() {
        return photos != null && !photos.isEmpty();
    }

    public int getPhotoCount() {
        return photos != null ? photos.size() : 0;
    }

    public UserAnimalPhoto getFirstPhoto() {
        if (hasPhotos()) {
            return photos.getFirst();
        }
        return null;
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
