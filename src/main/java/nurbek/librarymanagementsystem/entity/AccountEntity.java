package nurbek.librarymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "ACCOUNT")
public class AccountEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role = null;

    public Account toDto() {
        return new Account(id, firstName, lastName, email, password, role);
    }

    public static AccountEntity fromDto(Account account) {
        if (account == null) {
            return null;
        }
        return new AccountEntity(
                account.getId(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail(),
                account.getPassword(),
                account.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}