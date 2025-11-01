package com.itacademy.pokedex.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_animals")
@Getter
@Setter
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

    private String photoUrl;  // URL or filename of the uploaded photo
}
