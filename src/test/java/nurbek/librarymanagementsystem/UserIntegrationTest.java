package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.Role;
import nurbek.librarymanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes = LibraryManagementSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String URL = "/users";

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldUpdateUser() throws Exception {
        var account = new Account(
                null,
                "Updated Firstname",
                "Updated Lastname",
                "user1@gmail.com",
                "Password123!@", // invalid password format
                Role.USER
        );

        mockMvc.perform(patch(URL + "/1")
                        .flashAttr("updatedUser", account))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users/1"));

        var updatedAccount = userService.getAccountById(1L).orElse(null);
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getFirstName()).isEqualTo("Updated Firstname");
        assertThat(updatedAccount.getLastName()).isEqualTo("Updated Lastname");
        assertThat(updatedAccount.getEmail()).isEqualTo("user1@gmail.com");
        assertThat(updatedAccount.getRole()).isEqualTo(Role.USER);
        assertThat(updatedAccount.getPassword()).isNotEqualTo("Password123!@");
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldNotUpdateUserThatDoesNotExist() throws Exception {
        var account = new Account(
                null,
                "Updated Firstname",
                "Updated Lastname",
                "user1@gmail.com",
                "Password123!@", // invalid password format
                Role.USER
        );

        mockMvc.perform(patch(URL + "/100")
                        .flashAttr("updatedUser", account))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        var updatedAccount = userService.getAccountById(100L).orElse(null);
        assertThat(updatedAccount).isNull();
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldAddUser() throws Exception {
        var account = new Account(
                null,
                "Updated Firstname",
                "Updated Lastname",
                "user1@gmail.com",
                "Password123!@",
                Role.USER
        );

        mockMvc.perform(post(URL + "/add")
                        .flashAttr("newUser", account))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users"));

        var updatedAccount = userService.getAccountById(1001L).orElse(null);
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getFirstName()).isEqualTo(account.getFirstName());
        assertThat(updatedAccount.getLastName()).isEqualTo(account.getLastName());
        assertThat(updatedAccount.getEmail()).isEqualTo(account.getEmail());
        assertThat(updatedAccount.getRole()).isEqualTo(account.getRole());
    }

    @Test
    @WithUserDetails("ltownes5@google.pl")
    void shouldNotAddUserWithExistingEmail() throws Exception {
        var account = new Account(
                null,
                "Updated Firstname",
                "Updated Lastname",
                "fraine3@flickr.com", // existing email
                "Password123!@",
                Role.USER
        );

        mockMvc.perform(post(URL + "/add")
                        .flashAttr("newUser", account))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        var updatedAccount = userService.getAccountById(1001L).orElse(null);
        assertThat(updatedAccount).isNull();
    }
}