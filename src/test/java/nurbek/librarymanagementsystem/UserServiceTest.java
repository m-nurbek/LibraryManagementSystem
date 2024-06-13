package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.Role;
import nurbek.librarymanagementsystem.entity.AccountEntity;
import nurbek.librarymanagementsystem.repository.AccountRepository;
import nurbek.librarymanagementsystem.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldGetAccountByEmail() {
        // given
        var account = AccountEntity.fromDto(new Account(
                2L,
                "Firstname2",
                "Lastname2",
                "example@gmail.com",
                "password",
                Role.USER
        ));
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(account));

        // when
        var result = userService.getAccountByEmail("example@gmail.com").orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getEmail()).isEqualTo(account.getEmail());
    }

    @Test
    void shouldNotGetAccountByEmail() {
        // given
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        var result = userService.getAccountByEmail("example@gmail.com").orElse(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldSaveAccount() {
        // given
        var account = new Account(
                2L,
                "Firstname2",
                "Lastname2",
                "example@gmail.com",
                "password",
                Role.USER
        );
        var accountEntity = AccountEntity.fromDto(account);
        when(accountRepository.existsByEmail(anyString())).thenReturn(false);
        when(accountRepository.save(accountEntity)).thenReturn(accountEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        accountEntity.setPassword("encodedPassword");

        // when
        var result = userService.saveAccount(account).orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getEmail()).isEqualTo(account.getEmail());
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void shouldNotSaveAccount() {
        // given
        var account = new Account(
                2L,
                "Firstname2",
                "Lastname2",
                "example@gmail.com",
                "password",
                Role.USER
        );
        var accountEntity = AccountEntity.fromDto(account);
        when(accountRepository.existsByEmail(anyString())).thenReturn(true);

        // when
        var result = userService.saveAccount(account).orElse(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldUpdateAccountInfo() {
        // given
        var account = new Account(
                2L,
                "Firstname2",
                "Lastname2",
                "example@gmail.com",
                "password",
                Role.USER
        );
        var accountEntity = AccountEntity.fromDto(account);
        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.getReferenceById(anyLong())).thenReturn(accountEntity);
        accountEntity.setFirstName("Updated Firstname");
        accountEntity.setLastName("Updated Lastname");
        accountEntity.setEmail("updated@gmail.com");
        when(accountRepository.save(accountEntity)).thenReturn(accountEntity);

        // when
        var result = userService.updateAccountInfo(2L, account).orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(accountEntity.getFirstName());
        assertThat(result.getLastName()).isEqualTo(accountEntity.getLastName());
        assertThat(result.getEmail()).isEqualTo(accountEntity.getEmail());
    }

    @Test
    void shouldNotUpdateAccountInfo() {
        // given
        when(accountRepository.existsById(anyLong())).thenReturn(false);

        // when
        var result = userService.updateAccountInfo(2L, new Account()).orElse(null);

        // then
        assertThat(result).isNull();
    }
}