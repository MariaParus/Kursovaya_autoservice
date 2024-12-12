package com.example.autoservice.repository;

import com.example.autoservice.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    @Query("SELECT m FROM Maintenance m WHERE (:serviceName IS NULL OR m.serviceName LIKE %:serviceName%) AND (:minCost IS NULL OR m.cost >= :minCost) AND (:maxCost IS NULL OR m.cost <= :maxCost) AND (:serviceType IS NULL OR m.serviceType = :serviceType)")
    List<Maintenance> filterServices(@Param("serviceName") String serviceName, @Param("minCost") Double minCost, @Param("maxCost") Double maxCost, @Param("serviceType") String serviceType);

    @Query("SELECT m.serviceType, COUNT(m) FROM Maintenance m GROUP BY m.serviceType")
    List<Object[]> countServicesByServiceType();

    @Query("SELECT m.serviceType, AVG(m.cost) FROM Maintenance m GROUP BY m.serviceType")
    List<Object[]> getAverageCostByServiceType();

    List<Maintenance> findAllByOrderByCostAsc();

    List<Maintenance> findAllByOrderByCostDesc();
}