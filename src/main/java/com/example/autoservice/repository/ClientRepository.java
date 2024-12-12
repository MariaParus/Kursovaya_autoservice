package com.example.autoservice.repository;

import com.example.autoservice.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Client}.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Поиск клиентов по ключевому слову.
     *
     * @param keyword Ключевое слово для поиска.
     * @return Список клиентов, соответствующих ключевому слову.
     */
    @Query("SELECT c FROM Client c WHERE CONCAT(c.firstName, ' ', c.lastName, ' ', c.contactInfo) LIKE %?1%")
    List<Client> search(String keyword);

    /**
     * Подсчет количества клиентов по типу услуги.
     *
     * @return Список объектов, содержащих тип услуги и количество клиентов.
     */
    @Query("SELECT m.serviceType, COUNT(c) FROM Client c JOIN c.maintenance m GROUP BY m.serviceType")
    List<Object[]> countClientsByServiceType();
}
