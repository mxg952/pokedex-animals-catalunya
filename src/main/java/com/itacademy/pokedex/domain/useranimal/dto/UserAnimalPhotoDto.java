package com.itacademy.pokedex.domain.useranimal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnimalPhotoDto {

    private Long id;
    private String fileName;                    // ← CANVIAT
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String description;
    private LocalDateTime uploadedAt;
    private String downloadUrl;
    private String displayUrl;
    private Long userAnimalId;
    private Long animalId;
    private String animalCommonName;

    // Mètodes helpers
    public String getFormattedFileSize() {
        if (fileSize == null) return "0 KB";
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
    }

    public String getFormattedUploadDate() {
        if (uploadedAt == null) return "";
        return uploadedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public boolean isImage() {
        return contentType != null && contentType.startsWith("image/");
    }

    public String getFileExtension() {
        if (originalFileName == null) return "";
        int lastDot = originalFileName.lastIndexOf('.');
        return lastDot > 0 ? originalFileName.substring(lastDot + 1).toLowerCase() : "";
    }
}