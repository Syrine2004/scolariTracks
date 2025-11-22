package com.scolarite.backend.repositories;

import com.scolarite.backend.entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    // Spring Data crée automatiquement les méthodes save, findAll, findById, delete...
}