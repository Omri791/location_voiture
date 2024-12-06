package org.mdw31.tp4SOA.services;

import org.mdw31.tp4SOA.entitys.user;
import org.mdw31.tp4SOA.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080") // Autoriser CORS
@Path("/User")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Gérer les requêtes OPTIONS pour CORS
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

    @Autowired
    private PasswordEncoder passwordEncoder;  // Injecter le PasswordEncoder

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createUser(user user) {
        // Encoder le mot de passe avant de l'enregistrer
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // Remplacer le mot de passe original par le mot de passe encodé

        userRepository.save(user);
        return Response.status(Response.Status.CREATED)
                .entity("Utilisateur ajouté avec succès.")
                .build();
    }

    @POST
    @Path("/addTest")
    public Response addTest(@QueryParam("username") String username) {
        return Response.ok("Test POST reçu avec nom : " + username).build();
    }

    @DELETE
    @Path("/delete/{userId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser(@PathParam("userId") Long userId) {
        Optional<user> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.deleteById(userId);
            return Response.ok("Utilisateur supprimé avec succès.")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Utilisateur non trouvé.")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<user> users = userRepository.findAll();
        return Response.ok(users)
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                .build();
    }
    // Ajout de la méthode loginUser pour l'authentification
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response loginUser(user loginUser) {
        Optional<user> existingUser = userRepository.findByUsername(loginUser.getUsername()); // Trouver l'utilisateur par nom d'utilisateur

        if (existingUser.isPresent()) {
            // Vérifier le mot de passe
            user user = existingUser.get();
            if (passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
                // Mot de passe correct
                return Response.ok("Login réussi. Bienvenue " + user.getUsername())
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                        .build();
            } else {
                // Mot de passe incorrect
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Mot de passe incorrect.")
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                        .build();
            }
        } else {
            // Utilisateur non trouvé
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Utilisateur non trouvé.")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080")
                    .build();
        }
    }
}
