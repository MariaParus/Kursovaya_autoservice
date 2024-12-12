package com.example.autoservice.controller;

import com.example.autoservice.entity.Maintenance;
import com.example.autoservice.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class MaintenanceRestController {

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping
    public List<Maintenance> getAllServices() {
        return maintenanceService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getServiceById(@PathVariable Long id) {
        Maintenance service = maintenanceService.get(id);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service);
    }

    @PostMapping
    public ResponseEntity<Maintenance> createService(@RequestBody Maintenance service) {
        maintenanceService.save(service);
        return ResponseEntity.ok(service);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Maintenance> updateService(@PathVariable Long id, @RequestBody Maintenance serviceDetails) {
        Maintenance service = maintenanceService.get(id);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        service.setServiceName(serviceDetails.getServiceName());
        service.setCost(serviceDetails.getCost());
        service.setServiceType(serviceDetails.getServiceType());
        maintenanceService.save(service);
        return ResponseEntity.ok(service);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        Maintenance service = maintenanceService.get(id);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        maintenanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}