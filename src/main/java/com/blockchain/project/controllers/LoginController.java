package com.blockchain.project.controllers;

import com.blockchain.project.UserRepository;
import com.blockchain.project.models.User;
import com.blockchain.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login(){
        return "authPage";
    }

    @PostMapping("/addUser")
    public String addUser(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        if (userService.userExists(username)) {
            return "authIndex";
        }
        else
            model.addAttribute("message", true);
            return "redirect:/login";

    }

    @GetMapping("/allUsers")
    public List<User> getAllus() {
        return userService.getAllUsers();
    }
}
