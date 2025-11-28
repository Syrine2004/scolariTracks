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
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Component;

@Path("/etudiants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Étudiants", description = "Gestion des étudiants")
@Component
public class EtudiantResource {

    @Autowired
    private EtudiantService etudiantService;

    @Operation(summary = "Récupérer tous les étudiants")
    @GET
    public Response getAll(
            @Parameter(description = "Numéro de page (0-indexed)", example = "0") @DefaultValue("0") @QueryParam("page") int page,
            @Parameter(description = "Taille de page", example = "10") @DefaultValue("10") @QueryParam("size") int size,
            @Parameter(description = "Champ de tri", example = "nom") @DefaultValue("nom") @QueryParam("sortBy") String sortBy,
            @Parameter(description = "Direction du tri", example = "asc") @DefaultValue("asc") @QueryParam("sortDir") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Etudiant> etudiants = etudiantService.recupererTousLesEtudiants(pageable);
        return Response.ok(etudiants).build();
    }

    @Operation(summary = "Récupérer les étudiants d'une classe")
    @GET
    @Path("/classe/{classeId}")
    public Response getByClasse(
            @Parameter(description = "ID de la classe") @PathParam("classeId") Long classeId,
            @Parameter(description = "Numéro de page", example = "0") @DefaultValue("0") @QueryParam("page") int page,
            @Parameter(description = "Taille de page", example = "10") @DefaultValue("10") @QueryParam("size") int size,
            @Parameter(description = "Champ de tri", example = "nom") @DefaultValue("nom") @QueryParam("sortBy") String sortBy,
            @Parameter(description = "Direction du tri", example = "asc") @DefaultValue("asc") @QueryParam("sortDir") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Etudiant> etudiants = etudiantService.recupererParClasse(classeId, pageable);
        return Response.ok(etudiants).build();
    }

    @Operation(summary = "Créer un nouvel étudiant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Étudiant créé", content = @Content(schema = @Schema(implementation = Etudiant.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @POST
    public Response create(@Valid Etudiant etudiant) {
        Etudiant created = etudiantService.ajouterEtudiant(etudiant);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Operation(summary = "Récupérer un étudiant par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Étudiant trouvé", content = @Content(schema = @Schema(implementation = Etudiant.class))),
            @ApiResponse(responseCode = "404", description = "Étudiant introuvable", content = @Content)
    })
    @GET
    @Path("/{id}")
    public Response getOne(@Parameter(description = "ID de l'étudiant") @PathParam("id") Long id) {
        Etudiant etudiant = etudiantService.recupererParId(id);
        if (etudiant == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(etudiant).build();
    }

    @Operation(summary = "Mettre à jour un étudiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Étudiant mis à jour", content = @Content(schema = @Schema(implementation = Etudiant.class))),
            @ApiResponse(responseCode = "404", description = "Étudiant introuvable", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PUT
    @Path("/{id}")
    public Response update(@Parameter(description = "ID de l'étudiant") @PathParam("id") Long id,
            @Valid Etudiant etudiant) {
        Etudiant updated = etudiantService.modifierEtudiant(id, etudiant);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    @Operation(summary = "Supprimer un étudiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Étudiant supprimé"),
            @ApiResponse(responseCode = "404", description = "Étudiant introuvable", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DELETE
    @Path("/{id}")
    public Response delete(@Parameter(description = "ID de l'étudiant") @PathParam("id") Long id) {
        boolean deleted = etudiantService.supprimerEtudiant(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
