package com.scolarite.backend.services;

import com.scolarite.backend.entities.Etudiant;
import java.util.List;

public interface EtudiantService {
    Etudiant ajouterEtudiant(Etudiant etudiant);
    List<Etudiant> recupererTousLesEtudiants();
    Etudiant recupererParId(Long id);
    Etudiant modifierEtudiant(Long id, Etudiant etudiant);
    void supprimerEtudiant(Long id);
}