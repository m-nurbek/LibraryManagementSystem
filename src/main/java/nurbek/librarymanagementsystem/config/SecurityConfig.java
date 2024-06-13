package nurbek.librarymanagementsystem.config;

import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.entity.AccountEntity;
import nurbek.librarymanagementsystem.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return email -> {
            log.info("EMAIL: {}", email);
            AccountEntity account = accountRepository.findByEmail(email).orElse(null);

            if (account == null) {
                log.info("ACCOUNT NOT FOUND");
                throw new UsernameNotFoundException("User with email '" + email + "' not found");
            }
            log.info("ACCOUNT: {}", account.getUsername());
            return account;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("users/**").authenticated()
                        .anyRequest().permitAll())
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/library/books", true))
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }
}