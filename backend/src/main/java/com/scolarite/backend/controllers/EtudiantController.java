package com.scolarite.backend.controllers;

import com.scolarite.backend.entities.Etudiant;
import com.scolarite.backend.services.EtudiantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin("*")
@Tag(name = "Étudiants", description = "API pour la gestion des étudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @Operation(
        summary = "Récupérer tous les étudiants",
        description = "Récupère la liste de tous les étudiants avec pagination et tri optionnels"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
        @ApiResponse(responseCode = "401", description = "Non autorisé", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<Etudiant>> getAll(
            @Parameter(description = "Numéro de page (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Champ de tri (nom, prenom, email)", example = "nom")
            @RequestParam(defaultValue = "nom") String sortBy,
            @Parameter(description = "Direction du tri (asc, desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Etudiant> etudiants = etudiantService.recupererTousLesEtudiants(pageable);
        return ResponseEntity.ok(etudiants);
    }

    @Operation(
        summary = "Récupérer les étudiants d'une classe",
        description = "Récupère la liste des étudiants d'une classe spécifique avec pagination et tri"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
        @ApiResponse(responseCode = "404", description = "Classe non trouvée", content = @Content)
    })
    @GetMapping("/classe/{classeId}")
    public ResponseEntity<Page<Etudiant>> getByClasse(
            @Parameter(description = "ID de la classe")
            @PathVariable Long classeId,
            @Parameter(description = "Numéro de page (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Champ de tri (nom, prenom, email)", example = "nom")
            @RequestParam(defaultValue = "nom") String sortBy,
            @Parameter(description = "Direction du tri (asc, desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Etudiant> etudiants = etudiantService.recupererParClasse(classeId, pageable);
        return ResponseEntity.ok(etudiants);
    }

    @Operation(
        summary = "Créer un nouvel étudiant",
        description = "Ajoute un nouvel étudiant dans le système"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Étudiant créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
        @ApiResponse(responseCode = "403", description = "Accès refusé - Rôle ADMIN requis", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<Etudiant> create(@Valid @RequestBody Etudiant etudiant) {
        Etudiant created = etudiantService.ajouterEtudiant(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Récupérer un étudiant par ID",
        description = "Récupère les détails d'un étudiant spécifique"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Étudiant trouvé"),
        @ApiResponse(responseCode = "404", description = "Étudiant non trouvé", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getOne(
            @Parameter(description = "ID de l'étudiant")
            @PathVariable Long id) {
        Etudiant etudiant = etudiantService.recupererParId(id);
        if (etudiant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(etudiant);
    }

    @Operation(
        summary = "Modifier un étudiant",
        description = "Met à jour les informations d'un étudiant existant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Étudiant modifié avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
        @ApiResponse(responseCode = "404", description = "Étudiant non trouvé", content = @Content),
        @ApiResponse(responseCode = "403", description = "Accès refusé - Rôle ADMIN requis", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> update(
            @Parameter(description = "ID de l'étudiant")
            @PathVariable Long id, 
            @Valid @RequestBody Etudiant etudiant) {
        Etudiant updated = etudiantService.modifierEtudiant(id, etudiant);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Supprimer un étudiant",
        description = "Supprime un étudiant du système"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Étudiant supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Étudiant non trouvé", content = @Content),
        @ApiResponse(responseCode = "403", description = "Accès refusé - Rôle ADMIN requis", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de l'étudiant")
            @PathVariable Long id) {
        boolean deleted = etudiantService.supprimerEtudiant(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}