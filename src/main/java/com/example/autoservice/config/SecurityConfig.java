package com.example.autoservice.config;

import com.example.autoservice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Класс конфигурации безопасности приложения.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Конструктор класса SecurityConfig.
     *
     * @param userDetailsService Сервис для получения информации о пользователях.
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Метод для конфигурации цепочки фильтров безопасности.
     *
     * @param http Объект HttpSecurity для настройки безопасности.
     * @return Конфигурированная цепочка фильтров безопасности.
     * @throws Exception Если происходит ошибка при конфигурации.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/register", "/error/**").permitAll()
                        .requestMatchers("/api/**").permitAll() // Разрешаем доступ к RESTful API без авторизации
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/clients", "/new_client", "/edit_client", "/delete_client", "/new_service", "/edit_service", "/delete_service").hasRole("ADMIN")
                        .requestMatchers("/services", "/statistics", "/about_author").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/login?error=access_denied")
                );

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * Метод для создания бина PasswordEncoder.
     *
     * @return Кодировщик паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Метод для создания бина DaoAuthenticationProvider.
     *
     * @return Провайдер аутентификации.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
