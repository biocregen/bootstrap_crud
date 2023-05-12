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
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("userRoles", roleService.getAllRoles());
        model.addAttribute("userNew", new User());
        model.addAttribute("rolesNew", roleService.getAllRoles());
        return "admin";
    }
    @PostMapping("")
    public String addUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }
    @PatchMapping("/{id}")
    public String editUser(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        User userDb = userService.getById(id);
        userDb.setUsername(user.getUsername());
        userDb.setPassword(user.getPassword());
        userDb.setEmail(user.getEmail());
        userDb.setRoleList(user.getRoleList());
        userService.updateUser(userDb);
        return "redirect:/admin";
    }

    @GetMapping("{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
