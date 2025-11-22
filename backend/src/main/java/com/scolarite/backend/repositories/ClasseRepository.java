package com.scolarite.backend.repositories;

import com.scolarite.backend.entities.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // <--- Assure-toi que cette ligne est là

public interface ClasseRepository extends JpaRepository<Classe, Long> {

    // Spring Data JPA génère cette méthode automatiquement :
    Optional<Classe> findByNom(String nom);
}