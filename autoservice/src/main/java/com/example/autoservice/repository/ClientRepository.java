package com.example.autoservice.repository;

import com.example.autoservice.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c WHERE CONCAT(c.firstName, ' ', c.lastName, ' ', c.contactInfo) LIKE %?1%")
    List<Client> search(String keyword);

    @Query("SELECT m.serviceType, COUNT(c) FROM Client c JOIN c.maintenance m GROUP BY m.serviceType")
    List<Object[]> countClientsByServiceType();
}