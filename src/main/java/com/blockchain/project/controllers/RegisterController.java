package com.blockchain.project.controllers;

import ch.qos.logback.core.model.Model;
import com.blockchain.project.UserRepository;
import com.blockchain.project.models.User;
import com.blockchain.project.service.UserService;
import org.springframework.beans.propertyeditors.ReaderEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/register")
    public String register() {
        return "regPage";
    }

    @PostMapping("/createUser")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String walletAddress,
            RedirectAttributes redirectAttributes) {

        if (userService.userExists(username)) {
            redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует");
            return "redirect:/register";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole("USER");
        newUser.setWalletAddress(walletAddress); // Может быть null, если MetaMask не подключен

        userService.createUser(newUser);

        redirectAttributes.addFlashAttribute("success",
                walletAddress != null ?
                        "Регистрация успешна! Кошелек подключен: " + walletAddress :
                        "Регистрация успешна! Вы можете подключить кошелек позже");

        return "redirect:/authIndex";
    }

    @GetMapping("/connect-wallet")
    @ResponseBody
    public String connectWallet(@RequestParam String address) {
        // Можно добавить валидацию адреса
        return address;
    }
}
