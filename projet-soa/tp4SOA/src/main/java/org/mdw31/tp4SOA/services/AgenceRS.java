package org.mdw31.tp4SOA.services;

import org.mdw31.tp4SOA.entitys.Agence;
import org.mdw31.tp4SOA.entitys.Voiture;
import org.mdw31.tp4SOA.repositories.AgenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080") // Permet les requêtes CORS globales
@Path("/Agence")
public class AgenceRS {

    @Autowired
    private AgenceRepository repo;

    // Gérer CORS pour les requêtes préalables (OPTIONS)
    @OPTIONS
    @Path("{any:.*}")
    public Response handlePreflight() {
        return Response.ok()
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
                .build();
    }

    @POST
    @Path("/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response submitAgenceData(Agence agence) {
        repo.save(agence);
        return Response.ok("Agence reçue et traitée.")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .build();
    }

    @DELETE
    @Path("/delete/{agenceId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteAgenceData(@PathParam("agenceId") Long agenceId) {
        Optional<Agence> agence = repo.findById(agenceId);
        if (agence.isPresent()) {
            repo.deleteById(agenceId);
            return Response.ok("Agence supprimée avec succès")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Agence non trouvée")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStoredAgenceData() {
        List<Agence> agences = repo.findAll();
        return Response.ok(agences)
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .build();
    }


    @PUT
    @Path("/update/{agenceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateAgence(@PathParam("agenceId") Long agenceId, Agence agence) {
        Optional<Agence> existingAgence = repo.findById(agenceId);
        if (existingAgence.isPresent()) {
            Agence updatedAgence = existingAgence.get();
            updatedAgence.setNom(agence.getNom());
            updatedAgence.setAdresse(agence.getAdresse());
            repo.save(updatedAgence);
            return Response.ok("Agence modifiée avec succès")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Agence non trouvée")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }
}
