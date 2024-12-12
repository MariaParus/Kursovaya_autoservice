package com.example.autoservice.service;

import com.example.autoservice.entity.Maintenance;
import com.example.autoservice.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository repo;

    public List<Maintenance> listAll() {
        return repo.findAll();
    }

    public List<Maintenance> filterServices(String serviceName, Double minCost, Double maxCost, String serviceType) {
        return repo.filterServices(serviceName, minCost, maxCost, serviceType);
    }

    public void save(Maintenance maintenance) {
        repo.save(maintenance);
    }

    public Maintenance get(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public long countServices() {
        return repo.count();
    }

    public double getAverageCost() {
        List<Maintenance> services = repo.findAll();
        return services.stream().mapToDouble(Maintenance::getCost).average().orElse(0);
    }

    public Map<String, Long> countServicesByServiceType() {
        List<Object[]> results = repo.countServicesByServiceType();
        return results.stream().collect(Collectors.toMap(
                data -> (String) data[0],
                data -> ((Number) data[1]).longValue()
        ));
    }

    public Map<String, Double> getAverageCostByServiceType() {
        List<Object[]> results = repo.getAverageCostByServiceType();
        return results.stream().collect(Collectors.toMap(
                data -> (String) data[0],
                data -> ((Number) data[1]).doubleValue()
        ));
    }

    public List<Maintenance> sortServicesByCostAsc() {
        return repo.findAllByOrderByCostAsc();
    }

    public List<Maintenance> sortServicesByCostDesc() {
        return repo.findAllByOrderByCostDesc();
    }

    public List<Maintenance> getServicesWithAvailability(List<Maintenance> services) {
        return services.stream()
                .peek(service -> {
                    if (service.getClients() == null || service.getClients().isEmpty()) {
                        service.setOccupied(false);
                    } else {
                        service.setOccupied(true);
                    }
                })
                .collect(Collectors.toList());
    }
}
