package nurbek.librarymanagementsystem.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping
    public String allUsers(Model model, @PageableDefault(sort = {"title"}, size = 5) Pageable pageable) {
        Pageable pageProps = pageable;
        if (pageProps.getPageSize() <= 5) {
            pageProps = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
        }

        Page<Account> userList = userService.getAccountListByRole(Role.USER.name(), pageProps);
        model.addAttribute("users", userList);
        model.addAttribute("currentPage", pageProps.getPageNumber());
        model.addAttribute("totalPages", userList.getTotalPages());
        model.addAttribute("totalItems", userList.getTotalElements());

        return "users";
    }

    @GetMapping("/{id}")
    public String userDetail(Model model, @PathVariable("id") long id) {
        Account user = userService.getAccountById(id).orElse(null);
        if (user == null) {
            return "redirect:/users";
        }

        List<BookReservation> reservations = reservationService.getReservationsByAccountId(id);

        model.addAttribute("user", user);
        model.addAttribute("reservations", reservations);

        return "user";
    }

    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, Account user) {
        // Only allow updating users
        if (!user.getRole().equals(Role.USER)) {
            return "error";
        }
        Optional<Account> updatedUser = userService.updateAccount(id, user);
        if (updatedUser.isPresent()) {
            return "redirect:/users";
        }
        return "error";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute Account user) {
        // Only allow adding users
        if (!user.getRole().equals(Role.USER)) {
            return "error";
        }

        Optional<Account> savedUser = userService.saveAccount(user);
        if (savedUser.isPresent()) {
            return "redirect:/users";
        }
        return "error";
    }
}