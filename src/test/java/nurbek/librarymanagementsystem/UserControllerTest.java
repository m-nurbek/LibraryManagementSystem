package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.Role;
import nurbek.librarymanagementsystem.service.ReservationService;
import nurbek.librarymanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private ReservationService reservationService;
    private Page<Account> accountPage;
    private static final String URL = "/users";

    @BeforeEach
    void setUp() {
        accountPage = new PageImpl<>(List.of(
                new Account(1L, "Firstname", "Lastname", "firstlast@gmail.com", "password", Role.USER),
                new Account(2L, "Firstname2", "Lastname2", "example@gmail.com", "password", Role.USER),
                new Account(3L, "Firstname3", "Lastname3", "reno@gmail.com", "password", Role.USER),
                new Account(4L, "Firstname4", "Lastname4", "uyu@gmail.com", "password", Role.USER),
                new Account(5L, "Firstname5", "Lastname5", "ioer@gmail.com", "password", Role.USER),
                new Account(6L, "Firstname6", "Lastname6", "ido@gmail.com", "password", Role.USER),
                new Account(7L, "Firstname7", "Lastname7", "re@gmail.com", "password", Role.USER),
                new Account(8L, "Firstname8", "Lastname8", "eior@gmail.com", "password", Role.USER)
        ));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldReturnAllUsers() throws Exception {
        // given
        when(userService.getAccountListByRole(anyString(), any(Pageable.class))).thenReturn(accountPage);
        when(userService.getAccountByEmail(anyString())).thenReturn(Optional.of(
                new Account(1L, "Firstname", "Lastname", "firstlast@gmail.com", "password", Role.LIBRARIAN)
        ));

        // when
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("users", accountPage))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalItems", 8L));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldReturnAllUsersWithKeyword() throws Exception {
        // given
        when(userService.getAccountListByRole(anyString(), anyString(), any(Pageable.class))).thenReturn(accountPage);
        when(userService.getAccountByEmail(anyString())).thenReturn(Optional.of(
                new Account(1L, "Firstname", "Lastname", "firstlast@gmail.com", "password", Role.LIBRARIAN)
        ));

        // when
        mockMvc.perform(get(URL).param("keyword", "First"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("users", accountPage))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalItems", 8L));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldReturnUserDetail() throws Exception {
        // given
        when(userService.getAccountById(any(long.class))).thenReturn(Optional.of(
                new Account(1L, "User1", "LastnameUser1", "user1@gmail.com", "password", Role.USER)
        ));
        when(userService.getAccountByEmail(anyString())).thenReturn(Optional.of(
                new Account(2L, "Firstname", "Lastname", "firstlast@gmail.com", "password", Role.LIBRARIAN)
        ));
        when(reservationService.getReservationsByAccountId(any(long.class))).thenReturn(List.of());

        // when
        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(model().attributeExists("libraryUser", "reservations", "updatedUser", "account"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "USER")
    void shouldNotAccessAllUsersForUserRole() throws Exception {
        // when
        mockMvc.perform(get(URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "USER")
    void shouldNotAccessUserDetailForUserRole() throws Exception {
        // when
        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().isForbidden());
    }


    @Test
    void shouldNotAccessAllUsersForUnauthenticatedUser() throws Exception {
        // when
        mockMvc.perform(get(URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void shouldNotAccessUserDetailForUnauthenticatedUser() throws Exception {
        // when
        mockMvc.perform(get(URL + "/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldUpdateUser() throws Exception {
        // given
        Account account = new Account(51L, "User1", "LastnameUser1", "user1@gmail.com", "Password1!@", Role.USER);
        when(userService.getAccountByEmail(anyString())).thenReturn(Optional.of(
                new Account(50L, "Librarian1", "Librarian1", "librarian1@gmail.com", "password", Role.LIBRARIAN)
        ));
        when(userService.updateAccountInfo(anyLong(), any(Account.class))).thenReturn(Optional.of(account));

        // when
        mockMvc.perform(patch(URL + "/1")
                        .flashAttr("updatedUser", account))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/1"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldNotUpdateUserWithInvalidData() throws Exception {
        // given
        Account account = new Account(
                51L,
                "User1",
                "LastnameUser1",
                "user1@gmail.com",
                "password", // invalid password format
                Role.USER
        );
        when(userService.getAccountByEmail(anyString())).thenReturn(Optional.of(
                new Account(50L, "Librarian1", "Librarian1", "librarian1@gmail.com", "password", Role.LIBRARIAN)
        ));
        when(userService.getAccountById(anyLong())).thenReturn(Optional.of(account));
        when(reservationService.getReservationsByAccountId(any(long.class))).thenReturn(List.of());

        // when
        mockMvc.perform(patch(URL + "/1")
                        .flashAttr("updatedUser", account))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("libraryUser", "reservations"))
                .andExpect(view().name("user"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldAddUser() throws Exception {
        // given
        Account account = new Account(
                null,
                "User1",
                "User1",
                "user1@gmail.com",
                "Password!@32",
                Role.USER
        );
        when(userService.getAccountByEmail(anyString())).thenReturn(Optional.of(
                new Account(50L, "Librarian1", "Librarian1", "librarian1@gmail.com", "password", Role.LIBRARIAN)
        ));
        when(userService.saveAccount(any(Account.class))).thenReturn(Optional.of(account));

        // when
        mockMvc.perform(post(URL + "/add")
                        .flashAttr("newUser", account))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users"));
    }

    @Test
    @WithMockUser(value = "ltownes5@google.pl", username = "ltownes5@google.pl", roles = "LIBRARIAN")
    void shouldNotAddInvalidUser() throws Exception {
        // given
        Account account = new Account(
                51L,
                "User1",
                "User1",
                "user1@gmail.com",
                "password", // invalid password
                Role.USER
        );
        when(userService.getAccountByEmail(anyString())).thenReturn(Optional.of(
                new Account(50L, "Librarian1", "Librarian1", "librarian1@gmail.com", "password", Role.LIBRARIAN)
        ));
        when(userService.saveAccount(any(Account.class))).thenReturn(Optional.of(account));

        // when
        mockMvc.perform(post(URL + "/add")
                        .flashAttr("newUser", account))
                .andExpect(status().isOk())
                .andExpect(view().name("addUser"));
    }
}