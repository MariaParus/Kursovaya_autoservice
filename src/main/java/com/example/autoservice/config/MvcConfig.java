package com.example.autoservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс MvcConfig конфигурирует представления для различных URL.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Метод addViewControllers добавляет сопоставления URL с представлениями.
     *
     * @param registry Реестр контроллеров представлений.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/clients").setViewName("clients");
        registry.addViewController("/services").setViewName("services");
        registry.addViewController("/new_client").setViewName("new_client");
        registry.addViewController("/edit_client").setViewName("edit_client");
        registry.addViewController("/new_service").setViewName("new_service");
        registry.addViewController("/edit_service").setViewName("edit_service");
        registry.addViewController("/statistics").setViewName("statistics");
        registry.addViewController("/about_author").setViewName("about_author");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
    }
}
