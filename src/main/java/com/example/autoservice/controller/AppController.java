package com.example.autoservice.controller;

import com.example.autoservice.entity.Client;
import com.example.autoservice.service.ClientService;
import com.example.autoservice.entity.Maintenance;
import com.example.autoservice.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Основной контроллер приложения.
 */
@Controller
public class AppController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private MaintenanceService maintenanceService;

    /**
     * Отображает главную страницу.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения главной страницы.
     */
    @RequestMapping("/")
    public String viewHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "index";
    }

    /**
     * Отображает список клиентов.
     *
     * @param model   Модель для передачи данных в представление.
     * @param keyword Ключевое слово для поиска.
     * @return Имя представления для отображения списка клиентов.
     */
    @RequestMapping("/clients")
    @PreAuthorize("hasRole('ADMIN')")
    public String listClients(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        List<Client> listClients = clientService.listAll(keyword);
        model.addAttribute("listClients", listClients);
        model.addAttribute("keyword", keyword);
        return "clients";
    }

    /**
     * Отображает форму для добавления нового клиента.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения формы добавления нового клиента.
     */
    @RequestMapping("/new_client")
    public String showNewClientForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }
        Client client = new Client();
        List<Maintenance> listServices = clientService.getAllServicesWithAvailability();
        model.addAttribute("client", client);
        model.addAttribute("listServices", listServices);
        return "new_client";
    }

    /**
     * Сохраняет нового клиента.
     *
     * @param client Данные нового клиента.
     * @param model  Модель для передачи данных в представление.
     * @return Перенаправление на страницу со списком клиентов.
     */
    @RequestMapping(value = "/save_client", method = RequestMethod.POST)
    public String saveClient(@ModelAttribute("client") Client client, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }
        try {
            clientService.save(client);
            return "redirect:/clients";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<Maintenance> listServices = clientService.getAllServicesWithAvailability();
            model.addAttribute("client", client);
            model.addAttribute("listServices", listServices);
            return "new_client";
        }
    }

    /**
     * Отображает форму для редактирования клиента.
     *
     * @param id Идентификатор клиента.
     * @return Модель и представление для отображения формы редактирования клиента.
     */
    @RequestMapping("/edit_client/{id}")
    public ModelAndView showEditClientForm(@PathVariable(name = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/login?error=access_denied");
        }
        ModelAndView mav = new ModelAndView("edit_client");
        Client client = clientService.get(id);
        List<Maintenance> listServices = clientService.getAllServicesWithAvailability();
        mav.addObject("client", client);
        mav.addObject("listServices", listServices);
        return mav;
    }

    /**
     * Удаляет клиента по идентификатору.
     *
     * @param id Идентификатор клиента.
     * @return Перенаправление на страницу со списком клиентов.
     */
    @RequestMapping("/delete_client/{id}")
    public String deleteClient(@PathVariable(name = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }
        clientService.delete(id);
        return "redirect:/clients";
    }

    /**
     * Отображает список услуг.
     *
     * @param model      Модель для передачи данных в представление.
     * @param serviceName Название услуги для фильтрации.
     * @param minCost    Минимальная стоимость для фильтрации.
     * @param maxCost    Максимальная стоимость для фильтрации.
     * @param serviceType Тип услуги для фильтрации.
     * @param sort       Параметр сортировки.
     * @return Имя представления для отображения списка услуг.
     */
    @RequestMapping("/services")
    public String listServices(Model model, @RequestParam(name = "serviceName", required = false) String serviceName,
                               @RequestParam(name = "minCost", required = false) Double minCost,
                               @RequestParam(name = "maxCost", required = false) Double maxCost,
                               @RequestParam(name = "serviceType", required = false) String serviceType,
                               @RequestParam(name = "sort", required = false) String sort) {
        List<Maintenance> listServices;
        if (serviceName != null || minCost != null || maxCost != null || serviceType != null) {
            listServices = maintenanceService.filterServices(serviceName, minCost, maxCost, serviceType);
        } else {
            listServices = maintenanceService.listAll();
        }

        if ("asc".equals(sort)) {
            listServices = maintenanceService.sortServicesByCostAsc();
        } else if ("desc".equals(sort)) {
            listServices = maintenanceService.sortServicesByCostDesc();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            listServices = maintenanceService.getServicesWithAvailability(listServices);
        }

        model.addAttribute("listServices", listServices);
        model.addAttribute("serviceName", serviceName);
        model.addAttribute("minCost", minCost);
        model.addAttribute("maxCost", maxCost);
        model.addAttribute("serviceType", serviceType);
        model.addAttribute("isAdmin", isAdmin);
        return "services";
    }

    /**
     * Отображает форму для добавления новой услуги.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения формы добавления новой услуги.
     */
    @RequestMapping("/new_service")
    public String showNewServiceForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }
        Maintenance service = new Maintenance();
        model.addAttribute("service", service);
        return "new_service";
    }

    /**
     * Сохраняет новую услугу.
     *
     * @param service Данные новой услуги.
     * @return Перенаправление на страницу со списком услуг.
     */
    @RequestMapping(value = "/save_service", method = RequestMethod.POST)
    public String saveService(@ModelAttribute("service") Maintenance service) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }
        maintenanceService.save(service);
        return "redirect:/services";
    }

    /**
     * Отображает форму для редактирования услуги.
     *
     * @param id Идентификатор услуги.
     * @return Модель и представление для отображения формы редактирования услуги.
     */
    @RequestMapping("/edit_service/{id}")
    public ModelAndView showEditServiceForm(@PathVariable(name = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/login?error=access_denied");
        }
        ModelAndView mav = new ModelAndView("edit_service");
        Maintenance service = maintenanceService.get(id);
        mav.addObject("service", service);
        return mav;
    }

    /**
     * Удаляет услугу по идентификатору.
     *
     * @param id Идентификатор услуги.
     * @return Перенаправление на страницу со списком услуг.
     */
    @RequestMapping("/delete_service/{id}")
    public String deleteService(@PathVariable(name = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login?error=access_denied";
        }
        maintenanceService.delete(id);
        return "redirect:/services";
    }

    /**
     * Отображает страницу "Об авторе".
     *
     * @return Имя представления для отображения страницы "Об авторе".
     */
    @RequestMapping("/about_author")
    public String aboutAuthor() {
        return "about_author";
    }

    /**
     * Отображает страницу со статистикой.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения страницы со статистикой.
     */
    @RequestMapping("/statistics")
    public String viewStatistics(Model model) {
        long clientsCount = clientService.countClients();
        long servicesCount = maintenanceService.countServices();
        double averageCost = maintenanceService.getAverageCost();

        // Получение данных для гистограмм
        Map<String, Long> clientsByServiceType = clientService.countClientsByServiceType();
        Map<String, Long> servicesByServiceType = maintenanceService.countServicesByServiceType();
        Map<String, Double> averageCostByServiceType = maintenanceService.getAverageCostByServiceType();

        model.addAttribute("clientsCount", clientsCount);
        model.addAttribute("servicesCount", servicesCount);
        model.addAttribute("averageCost", averageCost);
        model.addAttribute("clientsByServiceType", clientsByServiceType);
        model.addAttribute("servicesByServiceType", servicesByServiceType);
        model.addAttribute("averageCostByServiceType", averageCostByServiceType);

        return "statistics";
    }
}
