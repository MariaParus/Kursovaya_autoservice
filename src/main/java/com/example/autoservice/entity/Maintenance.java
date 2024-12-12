package com.example.autoservice.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Сущность, представляющая услугу.
 */
@Entity
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceId;

    private String serviceName;
    private Double cost;
    private String serviceType;

    @OneToMany(mappedBy = "maintenance", fetch = FetchType.LAZY)
    private List<Client> clients;

    @Transient
    private boolean occupied;

    /**
     * Возвращает идентификатор услуги.
     *
     * @return Идентификатор услуги.
     */
    public Long getMaintenanceId() {
        return maintenanceId;
    }

    /**
     * Устанавливает идентификатор услуги.
     *
     * @param maintenanceId Идентификатор услуги.
     */
    public void setMaintenanceId(Long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    /**
     * Возвращает название услуги.
     *
     * @return Название услуги.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Устанавливает название услуги.
     *
     * @param serviceName Название услуги.
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Возвращает стоимость услуги.
     *
     * @return Стоимость услуги.
     */
    public Double getCost() {
        return cost;
    }

    /**
     * Устанавливает стоимость услуги.
     *
     * @param cost Стоимость услуги.
     */
    public void setCost(Double cost) {
        this.cost = cost;
    }

    /**
     * Возвращает тип услуги.
     *
     * @return Тип услуги.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Устанавливает тип услуги.
     *
     * @param serviceType Тип услуги.
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Возвращает список клиентов, связанных с услугой.
     *
     * @return Список клиентов, связанных с услугой.
     */
    public List<Client> getClients() {
        return clients;
    }

    /**
     * Устанавливает список клиентов, связанных с услугой.
     *
     * @param clients Список клиентов, связанных с услугой.
     */
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    /**
     * Возвращает статус занятости услуги.
     *
     * @return Статус занятости услуги.
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Устанавливает статус занятости услуги.
     *
     * @param occupied Статус занятости услуги.
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
