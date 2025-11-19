package com.itacademy.pokedex.domain.useranimal.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "user_animal_photos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnimalPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "original_filename", nullable = false)
    private String originalFileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "PENDING";

    @Column(name = "user_animal_id", nullable = false)
    private Long userAnimalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_animal_id", insertable = false, updatable = false)
    private UserAnimal userAnimal;

}
