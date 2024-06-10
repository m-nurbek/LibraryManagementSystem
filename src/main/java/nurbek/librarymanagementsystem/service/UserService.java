package nurbek.librarymanagementsystem.service;

import nurbek.librarymanagementsystem.dto.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    // TODO: Test this method
    Page<Account> getAccountList(Pageable pageable);

    // TODO: Test this method
    Page<Account> getAccountListByRole(String role, Pageable pageable);

    // TODO: Test this method
    Optional<Account> getAccountById(long id);

    // TODO: Test this method
    Optional<Account> getAccountByEmail(String email);

    // TODO: Test this method
    Optional<Account> saveAccount(Account account);

    // TODO: Test this method
    Optional<Account> updateAccountInfo(Long id, Account account);

    // TODO: Test this method
    /**
     * Deletes an account by id.
     * if the account is not found, returns false.
     * if the account is found and deleted, returns true.
     * @param id account id
     * @return true if the account is found and deleted, false if the account is not found
     */
    boolean deleteAccount(long id);
}