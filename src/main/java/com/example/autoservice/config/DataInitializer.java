package com.example.autoservice.config;

import com.example.autoservice.entity.Role;
import com.example.autoservice.entity.User;
import com.example.autoservice.repository.RoleRepository;
import com.example.autoservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс DataInitializer инициализирует базу данных с начальными данными, такими как роли и администраторский пользователь.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор класса DataInitializer.
     *
     * @param roleRepository   Репозиторий для работы с ролями.
     * @param userRepository   Репозиторий для работы с пользователями.
     * @param passwordEncoder  Кодировщик паролей.
     */
    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Метод run выполняется при запуске приложения и инициализирует базу данных.
     *
     * @param args Аргументы командной строки.
     * @throws Exception Если происходит ошибка при инициализации.
     */
    @Override
    public void run(String... args) throws Exception {
        Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
            Role role = new Role();
            role.setName("USER");
            return roleRepository.save(role);
        });

        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ADMIN");
            return roleRepository.save(role);
        });

        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            userRepository.save(admin);
        }
    }
}
