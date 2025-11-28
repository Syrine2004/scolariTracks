package com.scolarite.backend.controllers;

import com.scolarite.backend.entities.Classe;
import com.scolarite.backend.services.ClasseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.springframework.stereotype.Component;

@Path("/classes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Classes", description = "Gestion des classes")
@Component
public class ClasseResource {

    @Autowired
    private ClasseService classeService;

    @Operation(summary = "Récupérer toutes les classes")
    @GET
    public Response getAll() {
        List<Classe> classes = classeService.getAllClasses();
        return Response.ok(classes).build();
    }

    @Operation(summary = "Créer une nouvelle classe")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Classe créée", content = @Content(schema = @Schema(implementation = Classe.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @POST
    public Response create(Classe classe) {
        Classe created = classeService.createClasse(classe);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @Operation(summary = "Mettre à jour une classe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Classe mise à jour", content = @Content(schema = @Schema(implementation = Classe.class))),
            @ApiResponse(responseCode = "404", description = "Classe introuvable", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PUT
    @Path("/{id}")
    public Response update(
            @Parameter(description = "ID de la classe") @PathParam("id") Long id,
            Classe classe) {
        Classe updated = classeService.updateClasse(id, classe);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    @Operation(summary = "Supprimer une classe")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Classe supprimée"),
            @ApiResponse(responseCode = "404", description = "Classe introuvable", content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DELETE
    @Path("/{id}")
    public Response delete(@Parameter(description = "ID de la classe") @PathParam("id") Long id) {
        boolean deleted = classeService.deleteClasse(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
