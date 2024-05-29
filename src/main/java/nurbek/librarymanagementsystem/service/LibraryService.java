package nurbek.librarymanagementsystem.service;

import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {
    @Autowired
    private BookRepository bookRepository;

    public Page<BookEntity> getBookList(Pageable pageable) {
        return bookRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
    }

    public List<BookEntity> getBookList() {
        return bookRepository.findAll();
    }

    public BookEntity getBookById(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Page<BookEntity> searchBooksByKeyword(String keyword, Pageable pageable) {
        return bookRepository.searchByKeyword(keyword, PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        ));
    }
}