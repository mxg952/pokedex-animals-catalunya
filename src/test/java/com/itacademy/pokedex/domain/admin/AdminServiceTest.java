package com.itacademy.pokedex.domain.admin;

import com.itacademy.pokedex.domain.admin.dto.AdminStatsDto;
import com.itacademy.pokedex.domain.admin.service.AdminService;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.domain.useranimal.model.entity.UserAnimalPhoto;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalPhotoRepository;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAnimalRepository userAnimalRepository;

    @Mock
    private UserAnimalPhotoRepository userAnimalPhotoRepository;


    @InjectMocks
    private AdminService adminService;

    @Test
    void getStats_shouldReturnCorrectStatistics() {
        // 🔴 Given - Configurar mocks
        when(userRepository.count()).thenReturn(10L);
        when(userAnimalRepository.count()).thenReturn(25L);
        when(userAnimalPhotoRepository.count()).thenReturn(50L);

        // 🔴 When - Executar el mètode
        AdminStatsDto stats = adminService.getStats();

        // 🔴 Then - Verificar resultats (aquest test FALLARÀ inicialment)
        assertNotNull(stats);
        assertEquals(10L, stats.getTotalUsers());
        assertEquals(25L, stats.getTotalUnlockedAnimals());
        assertEquals(50L, stats.getTotalPhotos());
        assertNotNull(stats.getGeneratedAt());
    }

    @Test
    void deleteUserPhoto_shouldDeletePhotoWhenExists() {
        // 🔴 Given
        Long photoId = 1L;
        UserAnimalPhoto photo = UserAnimalPhoto.builder()
                .id(photoId)
                .fileName("photos/photo1.jpg")
                .build();

        when(userAnimalPhotoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        // 🔴 When
        adminService.deleteUserPhoto(photoId);

        // 🔴 Then - Verificar que s'ha cridat als mètodes de delete
        verify(userAnimalPhotoRepository).delete(photo);
        // verify(fileStorageService).deleteFile("photos/photo1.jpg");
    }
}

