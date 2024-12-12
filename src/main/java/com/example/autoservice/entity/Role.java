package com.example.autoservice.entity;

import jakarta.persistence.*;
import java.util.Set;

/**
 * Сущность, представляющая роль пользователя.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    /**
     * Возвращает идентификатор роли.
     *
     * @return Идентификатор роли.
     */
    public Long getId() {
        return id;
    }

    /**
     * Возвращает название роли.
     *
     * @return Название роли.
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название роли.
     *
     * @param name Название роли.
     */
    public void setName(String name) {
        this.name = name;
    }
}
