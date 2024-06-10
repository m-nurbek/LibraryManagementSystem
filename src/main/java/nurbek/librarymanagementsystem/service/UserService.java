package nurbek.librarymanagementsystem.service;

import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.entity.AccountEntity;
import nurbek.librarymanagementsystem.repository.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // TODO: Test this method
    public Page<Account> getAccountList(Pageable pageable) {
        Page<AccountEntity> accountEntities = accountRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "email"))
                ));

        return accountEntities.map(AccountEntity::toDto);
    }

    // TODO: Test this method
    public Page<Account> getAccountListByRole(String role, Pageable pageable) {
        Page<AccountEntity> accountEntities = accountRepository.getAccountsByRole(
                role,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "email"))
                ));

        return accountEntities.map(AccountEntity::toDto);
    }

    // TODO: Test this method
    public Optional<Account> getAccountById(long id) {
        AccountEntity account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            return Optional.of(account.toDto());
        }

        return Optional.empty();
    }

    // TODO: Test this method
    public Optional<Account> getAccountByEmail(String email) {
        AccountEntity account = accountRepository.findByEmail(email).orElse(null);
        if (account != null) {
            return Optional.of(account.toDto());
        }

        return Optional.empty();
    }

    // TODO: Test this method
    public Optional<Account> saveAccount(Account account) {
        AccountEntity accountEntity = AccountEntity.fromDto(account);
        if (accountRepository.existsByEmail(accountEntity.getEmail())) {
            return Optional.empty();
        }
        
        // Encode the password before saving
        accountEntity.setPassword(passwordEncoder.encode(account.getPassword()));

        accountEntity = accountRepository.save(accountEntity);
        return Optional.of(accountEntity.toDto());
    }

    // TODO: Test this method
    public Optional<Account> updateAccountInfo(Long id, Account account) {
        if (accountRepository.existsById(id)) {
            AccountEntity accountEntity = accountRepository.getReferenceById(id);
            accountEntity.setFirstName(account.getFirstName());
            accountEntity.setLastName(account.getLastName());
            accountEntity.setEmail(account.getEmail());
            accountEntity = accountRepository.save(accountEntity);
            return Optional.of(accountEntity.toDto());
        }

        return Optional.empty();
    }

    // TODO: Test this method

    /**
     * Deletes an account by id.
     * if the account is not found, returns false.
     * if the account is found and deleted, returns true.
     * @param id account id
     * @return true if the account is found and deleted, false if the account is not found
     */
    public boolean deleteAccount(long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}