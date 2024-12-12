package com.example.autoservice.entity;

import jakarta.persistence.*;

/**
 * Сущность, представляющая клиента.
 */
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    private String firstName;
    private String lastName;
    private String contactInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", insertable = true, updatable = true)
    private Maintenance maintenance;

    /**
     * Возвращает идентификатор клиента.
     *
     * @return Идентификатор клиента.
     */
    public Long getClientId() {
        return clientId;
    }

    /**
     * Устанавливает идентификатор клиента.
     *
     * @param clientId Идентификатор клиента.
     */
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    /**
     * Возвращает имя клиента.
     *
     * @return Имя клиента.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Устанавливает имя клиента.
     *
     * @param firstName Имя клиента.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Возвращает фамилию клиента.
     *
     * @return Фамилия клиента.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Устанавливает фамилию клиента.
     *
     * @param lastName Фамилия клиента.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Возвращает контактную информацию клиента.
     *
     * @return Контактная информация клиента.
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Устанавливает контактную информацию клиента.
     *
     * @param contactInfo Контактная информация клиента.
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Возвращает услугу, связанную с клиентом.
     *
     * @return Услуга, связанная с клиентом.
     */
    public Maintenance getMaintenance() {
        return maintenance;
    }

    /**
     * Устанавливает услугу, связанную с клиентом.
     *
     * @param maintenance Услуга, связанная с клиентом.
     */
    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }
}
