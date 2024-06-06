package nurbek.librarymanagementsystem.controller;

import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    private final UserService userService;

    @GetMapping
    public String home(Model model, Principal principal) {
        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("user", account);
        }
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("user", account);
        }
        return "login";
    }
}