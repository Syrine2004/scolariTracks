package com.scolarite.backend.repositories;

import com.scolarite.backend.entities.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    // Spring Data crée automatiquement les méthodes save, findAll, findById, delete...
    
    // Méthode pour trouver tous les étudiants d'une classe (avec pagination)
    Page<Etudiant> findByClasseId(Long classeId, Pageable pageable);
    
    // Méthode pour trouver tous les étudiants d'une classe (sans pagination - pour compatibilité)
    List<Etudiant> findByClasseId(Long classeId);
}