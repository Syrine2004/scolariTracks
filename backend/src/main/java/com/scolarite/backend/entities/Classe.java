package com.scolarite.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    // ON BRISE LA BOUCLE INFINIE ICI :
    @OneToMany(mappedBy = "classe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // <--- Empêche d'inclure la liste des étudiants quand on charge une classe
    private List<Etudiant> etudiants;

    // Champ calculé pour le nombre d'étudiants (nécessite Hibernate)
    @org.hibernate.annotations.Formula("(select count(*) from etudiant e where e.classe_id = id)")
    private Integer studentCount;
}