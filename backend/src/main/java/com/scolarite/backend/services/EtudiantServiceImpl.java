package com.scolarite.backend.services;

import com.scolarite.backend.entities.Etudiant;
import com.scolarite.backend.repositories.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Indique à Spring que c'est une classe de Logique Métier
public class EtudiantServiceImpl implements EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Override
    public Etudiant ajouterEtudiant(Etudiant etudiant) {
        // Ici, on pourrait ajouter des vérifications (ex: email déjà pris ?)
        return etudiantRepository.save(etudiant);
    }

    @Override
    public List<Etudiant> recupererTousLesEtudiants() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant recupererParId(Long id) {
        return etudiantRepository.findById(id).orElse(null);
    }

    @Override
    public Etudiant modifierEtudiant(Long id, Etudiant etudiantModifie) {
        return etudiantRepository.findById(id).map(etudiant -> {
            etudiant.setNom(etudiantModifie.getNom());
            etudiant.setPrenom(etudiantModifie.getPrenom());
            etudiant.setEmail(etudiantModifie.getEmail());
            etudiant.setClasse(etudiantModifie.getClasse());
            return etudiantRepository.save(etudiant);
        }).orElse(null);
    }

    @Override
    public void supprimerEtudiant(Long id) {
        etudiantRepository.deleteById(id);
    }
}