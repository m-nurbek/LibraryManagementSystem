package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
}