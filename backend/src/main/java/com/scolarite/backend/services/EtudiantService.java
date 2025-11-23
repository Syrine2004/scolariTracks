package com.scolarite.backend.services;

import com.scolarite.backend.entities.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EtudiantService {
    Etudiant ajouterEtudiant(Etudiant etudiant);
    Page<Etudiant> recupererTousLesEtudiants(Pageable pageable);
    Page<Etudiant> recupererParClasse(Long classeId, Pageable pageable);
    Etudiant recupererParId(Long id);
    Etudiant modifierEtudiant(Long id, Etudiant etudiant);
    boolean supprimerEtudiant(Long id);
}