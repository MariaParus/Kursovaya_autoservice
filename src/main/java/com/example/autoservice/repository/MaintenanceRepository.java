package com.example.autoservice.repository;

import com.example.autoservice.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Maintenance}.
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    /**
     * Фильтрация услуг по различным критериям.
     *
     * @param serviceName Название услуги.
     * @param minCost     Минимальная стоимость.
     * @param maxCost     Максимальная стоимость.
     * @param serviceType Тип услуги.
     * @return Список услуг, соответствующих заданным критериям.
     */
    @Query("SELECT m FROM Maintenance m WHERE (:serviceName IS NULL OR m.serviceName LIKE %:serviceName%) AND (:minCost IS NULL OR m.cost >= :minCost) AND (:maxCost IS NULL OR m.cost <= :maxCost) AND (:serviceType IS NULL OR m.serviceType = :serviceType)")
    List<Maintenance> filterServices(@Param("serviceName") String serviceName, @Param("minCost") Double minCost, @Param("maxCost") Double maxCost, @Param("serviceType") String serviceType);

    /**
     * Подсчет количества услуг по типу услуги.
     *
     * @return Список объектов, содержащих тип услуги и количество услуг.
     */
    @Query("SELECT m.serviceType, COUNT(m) FROM Maintenance m GROUP BY m.serviceType")
    List<Object[]> countServicesByServiceType();

    /**
     * Получение средней стоимости услуг по типу услуги.
     *
     * @return Список объектов, содержащих тип услуги и среднюю стоимость услуг.
     */
    @Query("SELECT m.serviceType, AVG(m.cost) FROM Maintenance m GROUP BY m.serviceType")
    List<Object[]> getAverageCostByServiceType();

    /**
     * Получение всех услуг, отсортированных по возрастанию стоимости.
     *
     * @return Список услуг, отсортированных по возрастанию стоимости.
     */
    List<Maintenance> findAllByOrderByCostAsc();

    /**
     * Получение всех услуг, отсортированных по убыванию стоимости.
     *
     * @return Список услуг, отсортированных по убыванию стоимости.
     */
    List<Maintenance> findAllByOrderByCostDesc();
}
