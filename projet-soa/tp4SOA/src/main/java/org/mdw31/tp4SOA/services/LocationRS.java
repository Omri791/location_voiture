package org.mdw31.tp4SOA.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mdw31.tp4SOA.entitys.Client;
import org.mdw31.tp4SOA.entitys.Location;
import org.mdw31.tp4SOA.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080") // Permet les requêtes CORS globales
@Path("/Location")
public class LocationRS {

    @Autowired
    private LocationRepository repo;

    // Gérer les requêtes préalables CORS (OPTIONS)
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
    public Response submitLocationData(Location location) {
        repo.save(location);
        return Response.ok("Location reçue et traitée.")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .build();
    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStoredLocationData() {
        List<Location> locations = repo.findAll();
        return Response.ok(locations)
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .build();
    }


    @DELETE
    @Path("/delete/{locationId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteLocationData(@PathParam("locationId") Long locationId) {
        Optional<Location> location = repo.findById(locationId);
        if (location.isPresent()) {
            repo.deleteById(locationId);
            return Response.ok("Location supprimée avec succès")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Location non trouvée")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }

    @PUT
    @Path("/update/{locationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateLocation(@PathParam("locationId") Long locationId, Location location) {
        Optional<Location> existingLocation = repo.findById(locationId);
        if (existingLocation.isPresent()) {
            Location updatedLocation = existingLocation.get();

            // Mise à jour des champs de la location
            updatedLocation.setDateDebut(location.getDateDebut());
            updatedLocation.setDateFin(location.getDateFin());

            // Sauvegarde de la location mise à jour
            repo.save(updatedLocation);

            return Response.ok("Location modifiée avec succès")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Location non trouvée")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }
}
