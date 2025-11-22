package com.scolarite.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- IMPORT IMPORTANT
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    // ON BRISE LA BOUCLE INFINIE ICI :
    @OneToMany(mappedBy = "classe")
    @JsonIgnore // <--- Empêche d'inclure la liste des étudiants quand on charge une classe
    private List<Etudiant> etudiants;
}