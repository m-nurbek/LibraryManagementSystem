package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    AccountEntity findByEmail(String email);
}