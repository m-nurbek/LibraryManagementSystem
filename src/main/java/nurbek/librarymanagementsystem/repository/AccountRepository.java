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
    @Query(value = "SELECT * FROM account", nativeQuery = true)
    Page<AccountEntity> findAll(PageRequest pageRequest);

    @Query(value = "SELECT * FROM account WHERE role = :role", nativeQuery = true)
    Page<AccountEntity> getAccountsByRole(@Param("role") String role, PageRequest pageRequest);

    @Query(value = """
            SELECT * FROM account AS a
            WHERE role = :role AND (
                    LOWER(a.email) LIKE LOWER('%' || :keyword || '%')
                    OR LOWER(CONCAT(a.first_name, a.last_name)) LIKE LOWER('%' || :keyword || '%')
            )""", nativeQuery = true)
    Page<AccountEntity> getAccountsByRoleAndKeyword(String keyword, @Param("role") String role, PageRequest pageRequest);

    @Query(value = "SELECT * FROM account WHERE email = :email", nativeQuery = true)
    Optional<AccountEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}