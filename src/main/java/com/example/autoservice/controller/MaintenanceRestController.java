package com.example.autoservice.controller;

import com.example.autoservice.entity.Maintenance;
import com.example.autoservice.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления услугами.
 */
@RestController
@RequestMapping("/api/services")
public class MaintenanceRestController {

    @Autowired
    private MaintenanceService maintenanceService;

    /**
     * Получает список всех услуг.
     *
     * @return Список всех услуг.
     */
    @GetMapping
    public List<Maintenance> getAllServices() {
        return maintenanceService.listAll();
    }

    /**
     * Получает услугу по идентификатору.
     *
     * @param id Идентификатор услуги.
     * @return Услуга, если найдена, иначе 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getServiceById(@PathVariable Long id) {
        Maintenance service = maintenanceService.get(id);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service);
    }

    /**
     * Создает новую услугу.
     *
     * @param service Данные новой услуги.
     * @return Созданная услуга.
     */
    @PostMapping
    public ResponseEntity<Maintenance> createService(@RequestBody Maintenance service) {
        maintenanceService.save(service);
        return ResponseEntity.ok(service);
    }

    /**
     * Обновляет существующую услугу.
     *
     * @param id            Идентификатор услуги.
     * @param serviceDetails Новые данные услуги.
     * @return Обновленная услуга, если найдена, иначе 404 Not Found.
     */
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

    /**
     * Удаляет услугу по идентификатору.
     *
     * @param id Идентификатор услуги.
     * @return 204 No Content, если услуга найдена и удалена, иначе 404 Not Found.
     */
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
