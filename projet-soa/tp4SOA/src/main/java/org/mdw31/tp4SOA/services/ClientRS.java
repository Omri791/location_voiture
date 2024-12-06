package org.mdw31.tp4SOA.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mdw31.tp4SOA.entitys.Client;
import org.mdw31.tp4SOA.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080") // Permet les requêtes CORS globales
@Path("/Client")
public class ClientRS {

    @Autowired
    private ClientRepository repo;

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
    public Response submitClientData(Client client) {
        repo.save(client);
        return Response.ok("Client received and processed.")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .build();
    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClients() {
        List<Client> clients = repo.findAll();
        return Response.ok(clients)
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .build();
    }

    @DELETE
    @Path("/delete/{clientId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteClient(@PathParam("clientId") String clientId) {
        Optional<Client> client = repo.findById(clientId);
        if (client.isPresent()) {
            repo.deleteById(clientId);
            return Response.ok("Client deleted successfully")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Client not found")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }

    @PUT
    @Path("/update/{clientId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClient(@PathParam("clientId") String clientId, Client client) {
        Optional<Client> existingClientOpt = repo.findById(clientId);

        if (existingClientOpt.isPresent()) {
            Client existingClient = existingClientOpt.get();

            // Mise à jour des champs du client
            existingClient.setNom(client.getNom());
            existingClient.setEmail(client.getEmail());
            existingClient.setTelephone(client.getTelephone());

            // Sauvegarde du client mis à jour
            repo.save(existingClient);

            return Response.ok(existingClient)
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Client not found")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }
}
