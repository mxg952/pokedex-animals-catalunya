package com.itacademy.pokedex.domain.useranimal.modelo.entity;

import com.itacademy.pokedex.domain.animal.modelo.entity.Animal;
import com.itacademy.pokedex.domain.useranimal.modelo.AnimalStatus;
import jakarta.persistence.*;
import lombok.*;

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

    private Long userId;  // Link to the user (storing just the ID for simplicity)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id")
    private Animal animal;  // Reference to the associated animal

    @Enumerated(EnumType.STRING)
    private AnimalStatus status;  // BLOCKED or UNLOCKED

    private List<> photoUrl;  // URL or filename of the uploaded photo
}
