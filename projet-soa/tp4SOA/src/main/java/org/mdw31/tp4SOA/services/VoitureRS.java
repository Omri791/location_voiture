package org.mdw31.tp4SOA.services;

import org.mdw31.tp4SOA.entitys.Voiture;
import org.mdw31.tp4SOA.repositories.VoitureRepository;
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
@Path("/Voiture")
public class VoitureRS {

    @Autowired
    private VoitureRepository repo;

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
    @Produces(MediaType.APPLICATION_JSON)  // Retourner une réponse en JSON
    public Response submitVoitureData(Voiture voiture) {
        // Vérification de l'agence_id (si nécessaire)
        if (voiture.getAgence() != null && !isValidAgence(voiture.getAgence().getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"L'agence spécifiée n'existe pas\"}")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }

        repo.save(voiture);
        return Response.ok("{\"message\": \"Voiture reçue et traitée.\"}")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .build();
    }

    @DELETE
    @Path("/delete/{voitureId}")
    @Produces(MediaType.APPLICATION_JSON)  // Retourner la réponse en JSON
    public Response deleteVoitureData(@PathParam("voitureId") Long voitureId) {
        Optional<Voiture> voiture = repo.findById(voitureId);
        if (voiture.isPresent()) {
            repo.deleteById(voitureId);
            return Response.ok("{\"message\": \"Voiture supprimée avec succès\"}")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Voiture non trouvée\"}")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStoredVoitureData() {
        List<Voiture> voitures = repo.findAll();
        return Response.ok(voitures)
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .build();
    }

    @PUT
    @Path("/update/{voitureId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)  // Retourner la réponse en JSON
    public Response updateVoiture(@PathParam("voitureId") Long voitureId, Voiture voiture) {
        Optional<Voiture> existingVoiture = repo.findById(voitureId);
        if (existingVoiture.isPresent()) {
            Voiture updatedVoiture = existingVoiture.get();
            updatedVoiture.setMarque(voiture.getMarque());
            updatedVoiture.setModele(voiture.getModele());
            updatedVoiture.setPrixParJour(voiture.getPrixParJour());

            // Vérification de l'agence si elle est définie
            if (voiture.getAgence() != null && !isValidAgence(voiture.getAgence().getId())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"L'agence spécifiée n'existe pas\"}")
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                        .build();
            }

            repo.save(updatedVoiture);
            return Response.ok("{\"message\": \"Voiture modifiée avec succès\"}")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Voiture non trouvée\"}")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }

    // Méthode pour vérifier si une agence est valide
    private boolean isValidAgence(Long agenceId) {
        // Remplacez par la logique réelle pour vérifier si l'agence existe
        return agenceId != null && agenceId > 0;
    }
}
