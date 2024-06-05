package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>, PagingAndSortingRepository<AccountEntity, Long> {
    Page<AccountEntity> findAll(PageRequest pageRequest);

    @Query(value = "SELECT * FROM account WHERE role = :role", nativeQuery = true)
    Page<AccountEntity> getAccountsByRole(@Param("role") String role, PageRequest pageRequest);

    Optional<AccountEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}