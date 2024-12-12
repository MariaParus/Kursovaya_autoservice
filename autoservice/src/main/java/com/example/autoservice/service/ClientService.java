package com.example.autoservice.service;

import com.example.autoservice.entity.Client;
import com.example.autoservice.entity.Maintenance;
import com.example.autoservice.repository.ClientRepository;
import com.example.autoservice.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repo;

    @Autowired
    private MaintenanceRepository maintenanceRepo;

    public List<Client> listAll(String keyword) {
        if (keyword != null) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }

    public void save(Client client) {
        if (client.getMaintenance() != null) {
            Maintenance maintenance = maintenanceRepo.findById(client.getMaintenance().getMaintenanceId()).orElse(null);
            if (maintenance != null) {
                List<Client> clients = maintenance.getClients();
                if (clients.stream().anyMatch(c -> !c.getClientId().equals(client.getClientId()))) {
                    throw new IllegalArgumentException("Услуга уже занята другим клиентом.");
                }
                clients.add(client);
                maintenance.setClients(clients);
                maintenance.setOccupied(true);
                maintenanceRepo.save(maintenance);
            }
        }
        repo.save(client);
    }

    public Client get(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public long countClients() {
        return repo.count();
    }

    public List<Maintenance> getAvailableServices() {
        List<Maintenance> allServices = maintenanceRepo.findAll();
        return allServices.stream()
                .filter(service -> service.getClients() == null || service.getClients().isEmpty())
                .collect(Collectors.toList());
    }

    public List<Maintenance> getAllServicesWithAvailability() {
        List<Maintenance> allServices = maintenanceRepo.findAll();
        return allServices.stream()
                .peek(service -> {
                    if (service.getClients() == null || service.getClients().isEmpty()) {
                        service.setOccupied(false);
                    } else {
                        service.setOccupied(true);
                    }
                })
                .collect(Collectors.toList());
    }

    public Map<String, Long> countClientsByServiceType() {
        List<Object[]> results = repo.countClientsByServiceType();
        return results.stream().collect(Collectors.toMap(
                data -> (String) data[0],
                data -> ((Number) data[1]).longValue()
        ));
    }
}