package nurbek.librarymanagementsystem.controller;

import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.BookReservation;
import nurbek.librarymanagementsystem.service.ReservationService;
import nurbek.librarymanagementsystem.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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