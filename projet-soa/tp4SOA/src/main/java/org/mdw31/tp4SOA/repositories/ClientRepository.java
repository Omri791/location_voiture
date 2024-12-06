package org.mdw31.tp4SOA.repositories;

import org.mdw31.tp4SOA.entitys.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
}

