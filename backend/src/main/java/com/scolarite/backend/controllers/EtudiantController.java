package com.scolarite.backend.controllers;

import com.scolarite.backend.entities.Etudiant;
import com.scolarite.backend.services.EtudiantService;
import jakarta.validation.Valid; // <--- IMPORT IMPORTANT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin("*")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @GetMapping
    public List<Etudiant> getAll() {
        return etudiantService.recupererTousLesEtudiants();
    }

    // AJOUT DE @Valid ICI
    @PostMapping
    public Etudiant create(@Valid @RequestBody Etudiant etudiant) {
        return etudiantService.ajouterEtudiant(etudiant);
    }

    @GetMapping("/{id}")
    public Etudiant getOne(@PathVariable Long id) {
        return etudiantService.recupererParId(id);
    }

    // AJOUT DE @Valid ICI AUSSI
    @PutMapping("/{id}")
    public Etudiant update(@PathVariable Long id, @Valid @RequestBody Etudiant etudiant) {
        return etudiantService.modifierEtudiant(id, etudiant);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        etudiantService.supprimerEtudiant(id);
    }
}