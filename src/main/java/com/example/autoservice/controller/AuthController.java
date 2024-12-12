package com.example.autoservice.controller;

import com.example.autoservice.entity.Role;
import com.example.autoservice.entity.User;
import com.example.autoservice.repository.RoleRepository;
import com.example.autoservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * Контроллер для управления аутентификацией и регистрацией пользователей.
 */
@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Отображает форму регистрации.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения формы регистрации.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Отображает форму входа.
     *
     * @return Имя представления для отображения формы входа.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param user   Данные нового пользователя.
     * @param result Результат валидации.
     * @param model  Модель для передачи данных в представление.
     * @return Перенаправление на страницу входа с сообщением об успешной регистрации.
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Имя пользователя уже существует");
            return "register";
        }

        // Шифрование пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Назначение роли "USER" по умолчанию
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Роль USER не найдена"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "redirect:/login?registerSuccess";
    }
}
