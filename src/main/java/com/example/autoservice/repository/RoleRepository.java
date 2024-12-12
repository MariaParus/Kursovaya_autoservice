package com.example.autoservice.repository;

import com.example.autoservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Role}.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по имени.
     *
     * @param name Имя роли.
     * @return Опциональный объект роли, если найдена.
     */
    Optional<Role> findByName(String name);
}
