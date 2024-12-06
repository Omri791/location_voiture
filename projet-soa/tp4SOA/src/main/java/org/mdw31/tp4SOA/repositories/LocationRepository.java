package org.mdw31.tp4SOA.repositories;

import org.mdw31.tp4SOA.entitys.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    // Vous pouvez ajouter des méthodes personnalisées ici si nécessaire
}
