package nurbek.librarymanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.BookReservation;
import nurbek.librarymanagementsystem.dto.Role;
import nurbek.librarymanagementsystem.property.ConfigurationProperty;
import nurbek.librarymanagementsystem.service.ReservationService;
import nurbek.librarymanagementsystem.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


// TODO: test this controller
@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ReservationService reservationService;
    private final int pageSize;

    public UserController(UserService userService, ReservationService reservationService, ConfigurationProperty props) {
        this.userService = userService;
        this.reservationService = reservationService;
        pageSize = props.getPageSize();
    }

    @Secured("ROLE_LIBRARIAN")
    @GetMapping
    public String allUsers(Model model, @PageableDefault(sort = {"email"}, size = 5) Pageable pageable, Principal principal) {
        Pageable pageProps = pageable;
        if (pageProps.getPageSize() <= 5) {
            pageProps = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
        }

        Page<Account> userList = userService.getAccountListByRole(Role.USER.name(), pageProps);
        log.info(userList.getContent().toString());
        model.addAttribute("users", userList);
        model.addAttribute("currentPage", pageProps.getPageNumber());
        model.addAttribute("totalPages", userList.getTotalPages());
        model.addAttribute("totalItems", userList.getTotalElements());

        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("account", account);
        }

        return "users";
    }

    @Secured("ROLE_LIBRARIAN")
    @GetMapping("/{id}")
    public String userDetail(Model model, @PathVariable("id") long id, Principal principal) {
        Account user = userService.getAccountById(id).orElse(null);
        if (user == null) {
            return "redirect:/users";
        }

        List<BookReservation> reservations = reservationService.getReservationsByAccountId(id);

        model.addAttribute("user", user);
        model.addAttribute("reservations", reservations);

        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("account", account);
        }

        return "user";
    }

    @Secured("ROLE_LIBRARIAN")
    @PatchMapping("/{id}")
    public String updateUser(@PathVariable("id") long id, @ModelAttribute("user") Account user) {
        Optional<Account> updatedUser = userService.updateAccountInfo(id, user);

        if (updatedUser.isPresent()) {
            return "redirect:/users/" + id;
        }

        return "error";
    }

    @Secured("ROLE_LIBRARIAN")
    @GetMapping("/add")
    public String addUserForm(Model model, Principal principal) {
        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("account", account);
        }

        model.addAttribute("newUser", new Account());
        model.addAttribute("roles", Role.values());

        return "addUser";
    }

    @Secured("ROLE_LIBRARIAN")
    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("newUser") Account newUser, Errors errors, Model model, Principal principal) {
        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("account", account);
        }

        if (errors.hasErrors()) {
            return "addUser";
        }

        Optional<Account> savedUser = userService.saveAccount(newUser);
        if (savedUser.isPresent()) {
            return "redirect:/users";
        }

        return "error";
    }
}