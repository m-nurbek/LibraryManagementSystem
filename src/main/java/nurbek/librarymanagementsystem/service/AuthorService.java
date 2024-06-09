package nurbek.librarymanagementsystem.service;

import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Author;
import nurbek.librarymanagementsystem.entity.AuthorEntity;
import nurbek.librarymanagementsystem.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll().stream().map(AuthorEntity::toDto).toList();
    }

    public Optional<Author> getAuthorById(long id) {
        return authorRepository.findById(id).map(AuthorEntity::toDto);
    }
}