package org.mdw31.tp4SOA.repositories;

import org.mdw31.tp4SOA.entitys.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
    // Vous pouvez ajouter des méthodes personnalisées ici si nécessaire
}
