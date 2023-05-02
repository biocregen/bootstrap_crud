package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping
    public String mainPage(Principal principal, Model model) {
        User admin = userService.getByUsername(principal.getName());
        model.addAttribute("admin", admin);
        return "admin";
    }
    @GetMapping("/all-users")
    public String getAllUsers (ModelMap model) {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("usersRoles", roleService.getAllRoles());
        return "all-users";
    }
    @GetMapping("user/{id}")
    public String showUser(@PathVariable("id") int id, Model model){
        model.addAttribute("users", userService.getById(id));
        return "/user";
    }
    @GetMapping("/add")
    public String addForm(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "add";
    }
    @PostMapping("/add")
    public String addSubmit(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin/all-users";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin/all-users";
    }
    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, ModelMap model){
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("role",roleService.getAllRoles());
        return "edit";
    }
    @PatchMapping("/{id}")
    public String editSubmit(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/all-users";
    }

}
