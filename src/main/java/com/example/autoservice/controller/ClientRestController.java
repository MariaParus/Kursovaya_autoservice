package com.example.autoservice.controller;

import com.example.autoservice.entity.Client;
import com.example.autoservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления клиентами.
 */
@RestController
@RequestMapping("/api/clients")
public class ClientRestController {

    @Autowired
    private ClientService clientService;

    /**
     * Получает список всех клиентов.
     *
     * @return Список всех клиентов.
     */
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.listAll(null);
    }

    /**
     * Получает клиента по идентификатору.
     *
     * @param id Идентификатор клиента.
     * @return Клиент, если найден, иначе 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.get(id);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }

    /**
     * Создает нового клиента.
     *
     * @param client Данные нового клиента.
     * @return Созданный клиент.
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        clientService.save(client);
        return ResponseEntity.ok(client);
    }

    /**
     * Обновляет существующего клиента.
     *
     * @param id            Идентификатор клиента.
     * @param clientDetails Новые данные клиента.
     * @return Обновленный клиент, если найден, иначе 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {
        Client client = clientService.get(id);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        client.setFirstName(clientDetails.getFirstName());
        client.setLastName(clientDetails.getLastName());
        client.setContactInfo(clientDetails.getContactInfo());
        client.setMaintenance(clientDetails.getMaintenance());
        clientService.save(client);
        return ResponseEntity.ok(client);
    }

    /**
     * Удаляет клиента по идентификатору.
     *
     * @param id Идентификатор клиента.
     * @return 204 No Content, если клиент найден и удален, иначе 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        Client client = clientService.get(id);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
