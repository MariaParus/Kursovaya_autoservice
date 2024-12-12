package com.example.autoservice.entity;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Сущность, представляющая пользователя.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя пользователя обязательно")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Пароль обязателен")
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /**
     * Возвращает идентификатор пользователя.
     *
     * @return Идентификатор пользователя.
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор пользователя.
     *
     * @param id Идентификатор пользователя.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return Имя пользователя.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя.
     *
     * @param username Имя пользователя.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя.
     *
     * @param password Пароль пользователя.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Возвращает роли пользователя.
     *
     * @return Роли пользователя.
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Устанавливает роли пользователя.
     *
     * @param roles Роли пользователя.
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
