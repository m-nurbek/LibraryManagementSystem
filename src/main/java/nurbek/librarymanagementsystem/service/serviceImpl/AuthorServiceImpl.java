package nurbek.librarymanagementsystem.service.serviceImpl;

import lombok.AllArgsConstructor;
import nurbek.librarymanagementsystem.dto.Author;
import nurbek.librarymanagementsystem.entity.AuthorEntity;
import nurbek.librarymanagementsystem.repository.AuthorRepository;
import nurbek.librarymanagementsystem.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll().stream().map(AuthorEntity::toDto).toList();
    }

    @Override
    public Optional<Author> getAuthorById(long id) {
        return authorRepository.findById(id).map(AuthorEntity::toDto);
    }
}