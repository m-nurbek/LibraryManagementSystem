package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
}