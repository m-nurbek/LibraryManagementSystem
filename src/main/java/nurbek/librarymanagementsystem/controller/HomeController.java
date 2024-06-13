package nurbek.librarymanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.BookReservation;
import nurbek.librarymanagementsystem.service.ReservationService;
import nurbek.librarymanagementsystem.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    private final UserService userService;
    private final ReservationService reservationService;

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

    @GetMapping("/register")
    public String register(Model model, Principal principal) {
        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("account", account);
        }

        model.addAttribute("newAccount", new Account());

        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("newAccount") Account newAccount, Errors errors, Model model, Principal principal) {
        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("account", account);
        }

        if (errors.hasErrors()) {
            return "register";
        }

        Optional<Account> savedUser = userService.saveAccount(newAccount);

        if (savedUser.isPresent()) {
            return "redirect:/login";
        }

        return "error";
    }

    @Secured({"ROLE_LIBRARIAN", "ROLE_USER"})
    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) {
        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            List<BookReservation> bookReservations = account != null ? reservationService.getReservationsByAccountId(account.getId()) : new ArrayList<>();

            model.addAttribute("account", account);
            model.addAttribute("reservations", bookReservations);

            return "profile";
        }

        return "error";
    }
}