package com.itacademy.pokedex.domain.admin;

import com.itacademy.pokedex.domain.admin.dto.AdminStatsDto;
import com.itacademy.pokedex.domain.admin.dto.CreateAnimalRequest;
import com.itacademy.pokedex.domain.admin.dto.UserInfoDto;
import com.itacademy.pokedex.domain.admin.service.AdminService;
import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.animal.repository.AnimalRepository;
import com.itacademy.pokedex.domain.user.modelo.entity.User;
import com.itacademy.pokedex.domain.user.repository.UserRepository;
import com.itacademy.pokedex.domain.useranimal.model.AnimalStatus;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalPhotoRepository;
import com.itacademy.pokedex.domain.useranimal.repository.UserAnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAnimalRepository userAnimalRepository;

    @Mock
    private UserAnimalPhotoRepository userAnimalPhotoRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AdminService adminService;

    private User testUser;
    private Animal testAnimal;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .createdAt(LocalDateTime.now())
                .build();

        testAnimal = Animal.builder()
                .id(1L)
                .commonName("Test Animal")
                .scientificName("Testus Animalis")
                .build();
    }

    @Test
    void getStats_ShouldReturnCorrectStats() {
        when(userRepository.count()).thenReturn(10L);
        when(userAnimalRepository.count()).thenReturn(50L);
        when(userAnimalPhotoRepository.count()).thenReturn(100L);

        AdminStatsDto result = adminService.getStats();

        assertNotNull(result);
        assertEquals(10L, result.getTotalUsers());
        assertEquals(50L, result.getTotalUnlockedAnimals());
        assertEquals(100L, result.getTotalPhotos());
        assertNotNull(result.getGeneratedAt());
    }

    @Test
    void getAllUsers_ShouldReturnUserListWithStats() {
        List<User> users = Collections.singletonList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        when(userAnimalRepository.countByUserIdAndStatus(1L, AnimalStatus.UNLOCK)).thenReturn(5L);
        when(userAnimalPhotoRepository.countByUserId(1L)).thenReturn(10L);

        List<UserInfoDto> result = adminService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());

        UserInfoDto userInfo = result.get(0);
        assertEquals(1L, userInfo.getId());
        assertEquals("Test User", userInfo.getName());
        assertEquals(5L, userInfo.getUnlockedAnimals());
        assertEquals(10L, userInfo.getUploadedPhotos());
    }

    @Test
    void createAnimal_ShouldCreateAnimalSuccessfully() throws Exception {
        CreateAnimalRequest request = CreateAnimalRequest.builder()
                .commonName("Test Animal")
                .scientificName("Testus Animalis")
                .category("Mammal")
                .visibilityProbability("High")
                .sightingMonths("January,February,March")
                .shortDescription("Test description")
                .locationDescription("Test location")
                .mapUrl("http://test.map")
                .build();

        when(animalRepository.save(any(Animal.class))).thenReturn(testAnimal);

        Animal result = adminService.createAnimal(request);

        assertNotNull(result);
        assertEquals("Test Animal", result.getCommonName());
        verify(animalRepository, times(1)).save(any(Animal.class));
    }
}

