package com.example.autoservice.service;

import com.example.autoservice.entity.Maintenance;
import com.example.autoservice.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для управления услугами.
 */
@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository repo;

    /**
     * Возвращает список всех услуг.
     *
     * @return Список всех услуг.
     */
    public List<Maintenance> listAll() {
        return repo.findAll();
    }

    /**
     * Фильтрует услуги по различным критериям.
     *
     * @param serviceName Название услуги.
     * @param minCost     Минимальная стоимость.
     * @param maxCost     Максимальная стоимость.
     * @param serviceType Тип услуги.
     * @return Список услуг, соответствующих заданным критериям.
     */
    public List<Maintenance> filterServices(String serviceName, Double minCost, Double maxCost, String serviceType) {
        return repo.filterServices(serviceName, minCost, maxCost, serviceType);
    }

    /**
     * Сохраняет услугу.
     *
     * @param maintenance Услуга для сохранения.
     */
    public void save(Maintenance maintenance) {
        repo.save(maintenance);
    }

    /**
     * Возвращает услугу по идентификатору.
     *
     * @param id Идентификатор услуги.
     * @return Услуга, если найдена, иначе null.
     */
    public Maintenance get(Long id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * Удаляет услугу по идентификатору.
     *
     * @param id Идентификатор услуги.
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Возвращает количество услуг.
     *
     * @return Количество услуг.
     */
    public long countServices() {
        return repo.count();
    }

    /**
     * Возвращает среднюю стоимость всех услуг.
     *
     * @return Средняя стоимость всех услуг.
     */
    public double getAverageCost() {
        List<Maintenance> services = repo.findAll();
        return services.stream().mapToDouble(Maintenance::getCost).average().orElse(0);
    }

    /**
     * Возвращает количество услуг по типу услуги.
     *
     * @return Карта, где ключ - тип услуги, значение - количество услуг.
     */
    public Map<String, Long> countServicesByServiceType() {
        List<Object[]> results = repo.countServicesByServiceType();
        return results.stream().collect(Collectors.toMap(
                data -> (String) data[0],
                data -> ((Number) data[1]).longValue()
        ));
    }

    /**
     * Возвращает среднюю стоимость услуг по типу услуги.
     *
     * @return Карта, где ключ - тип услуги, значение - средняя стоимость услуг.
     */
    public Map<String, Double> getAverageCostByServiceType() {
        List<Object[]> results = repo.getAverageCostByServiceType();
        return results.stream().collect(Collectors.toMap(
                data -> (String) data[0],
                data -> ((Number) data[1]).doubleValue()
        ));
    }

    /**
     * Возвращает список услуг, отсортированных по возрастанию стоимости.
     *
     * @return Список услуг, отсортированных по возрастанию стоимости.
     */
    public List<Maintenance> sortServicesByCostAsc() {
        return repo.findAllByOrderByCostAsc();
    }

    /**
     * Возвращает список услуг, отсортированных по убыванию стоимости.
     *
     * @return Список услуг, отсортированных по убыванию стоимости.
     */
    public List<Maintenance> sortServicesByCostDesc() {
        return repo.findAllByOrderByCostDesc();
    }

    /**
     * Возвращает список услуг с информацией о доступности.
     *
     * @param services Список услуг.
     * @return Список услуг с информацией о доступности.
     */
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
