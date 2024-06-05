package nurbek.librarymanagementsystem.service;

import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.entity.AccountEntity;
import nurbek.librarymanagementsystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private AccountRepository accountRepository;

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
        accountEntity = accountRepository.save(accountEntity);
        return Optional.of(accountEntity.toDto());
    }

    // TODO: Test this method
    public Optional<Account> updateAccount(Long id, Account account) {
        if (accountRepository.existsById(id)) {
            AccountEntity accountEntity = AccountEntity.fromDto(account);
            accountEntity.setId(id);
            accountEntity = accountRepository.save(accountEntity);
            return Optional.of(accountEntity.toDto());
        }
        return Optional.empty();
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
}