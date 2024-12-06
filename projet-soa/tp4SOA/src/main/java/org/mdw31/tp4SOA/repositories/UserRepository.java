package org.mdw31.tp4SOA.repositories;
import org.mdw31.tp4SOA.entitys.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<user, Long> {
    // Ajouter la méthode pour chercher un utilisateur par son nom d'utilisateur
    Optional<user> findByUsername(String username);
}
