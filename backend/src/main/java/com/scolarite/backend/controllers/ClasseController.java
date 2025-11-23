package com.scolarite.backend.controllers;

import com.scolarite.backend.entities.Classe;
import com.scolarite.backend.services.ClasseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin("*")
@Tag(name = "Classes", description = "API pour la gestion des classes")
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @Operation(
        summary = "Récupérer toutes les classes",
        description = "Récupère la liste de toutes les classes"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    @GetMapping
    public ResponseEntity<List<Classe>> getAll() {
        return ResponseEntity.ok(classeService.getAllClasses());
    }

    @Operation(
        summary = "Créer une nouvelle classe",
        description = "Ajoute une nouvelle classe dans le système"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Classe créée avec succès"),
        @ApiResponse(responseCode = "403", description = "Accès refusé - Rôle ADMIN requis", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<Classe> create(@RequestBody Classe classe) {
        Classe created = classeService.createClasse(classe);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Modifier une classe",
        description = "Met à jour les informations d'une classe existante"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classe modifiée avec succès"),
        @ApiResponse(responseCode = "404", description = "Classe non trouvée", content = @Content),
        @ApiResponse(responseCode = "403", description = "Accès refusé - Rôle ADMIN requis", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public ResponseEntity<Classe> update(
            @Parameter(description = "ID de la classe")
            @PathVariable Long id, 
            @RequestBody Classe classe) {
        Classe updated = classeService.updateClasse(id, classe);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Supprimer une classe",
        description = "Supprime une classe du système (supprime également tous les étudiants associés)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Classe supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Classe non trouvée", content = @Content),
        @ApiResponse(responseCode = "403", description = "Accès refusé - Rôle ADMIN requis", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la classe")
            @PathVariable Long id) {
        boolean deleted = classeService.deleteClasse(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}