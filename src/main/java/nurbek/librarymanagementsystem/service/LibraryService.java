package nurbek.librarymanagementsystem.service;

import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {
    @Autowired
    private BookRepository bookRepository;

    public List<BookEntity> getBookList() {
        return (List<BookEntity>) bookRepository.findAll();
    }
}