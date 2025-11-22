package com.scolarite.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Email(message = "Format d'email invalide")
    @Column(unique = true)
    private String email;

    // --- CORRECTION POUR LE GUIDE (Relation) ---
    // Au lieu d'un texte simple, on lie à la table Classe
    @ManyToOne
    @JoinColumn(name = "classe_id") // Crée une colonne clé étrangère
    private Classe classe;
}