package com.scolarite.backend.services;

import com.scolarite.backend.entities.Etudiant;
import com.scolarite.backend.repositories.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Etudiant> recupererTousLesEtudiants(Pageable pageable) {
        return etudiantRepository.findAll(pageable);
    }

    @Override
    public Page<Etudiant> recupererParClasse(Long classeId, Pageable pageable) {
        return etudiantRepository.findByClasseId(classeId, pageable);
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
    public boolean supprimerEtudiant(Long id) {
        if (etudiantRepository.existsById(id)) {
            etudiantRepository.deleteById(id);
            return true;
        }
        return false;
    }
}