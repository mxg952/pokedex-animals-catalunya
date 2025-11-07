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

    @Column(name = "user_animal_id", nullable = false)
    private Long userAnimalId;

    // Relació ManyToOne amb UserAnimal (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_animal_id", insertable = false, updatable = false)
    private UserAnimal userAnimal;

    @PrePersist
    protected void onCreate() {
        if (uploadedAt == null) {
            uploadedAt = LocalDateTime.now();
        }
    }

    // Mètodes helpers
    public boolean isImage() {
        return contentType != null && contentType.startsWith("image/");
    }

    public String getFileExtension() {
        if (originalFileName == null) return "";
        int lastDot = originalFileName.lastIndexOf('.');
        return lastDot > 0 ? originalFileName.substring(lastDot + 1).toLowerCase() : "";
    }

    public String getFormattedFileSize() {
        if (fileSize == null) return "0 KB";
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
    }

    // Mètode per obtenir el path complet
    public String getFullPath(String uploadDir) {
        return uploadDir + "/" + fileName;
    }
}
