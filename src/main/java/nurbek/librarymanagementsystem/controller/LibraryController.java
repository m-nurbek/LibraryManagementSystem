package nurbek.librarymanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Slf4j
@Controller
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping(path = {"/books"})
    public String libraryBooks(Model model, @PageableDefault(sort = {"title"}, value = 10) Pageable pageable,
                               @RequestParam(value = "keyword", required = false) String keyword) {
        Page<Book> bookList;
        if (keyword != null && !keyword.isEmpty()) {
            bookList = libraryService.searchBooksByKeyword(keyword, pageable);
            model.addAttribute("books", bookList);
        } else {
            bookList = libraryService.getBookList(pageable);
            model.addAttribute("books", bookList);
        }
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", bookList.getTotalPages());
        model.addAttribute("totalItems", bookList.getTotalElements());

        return "books";
    }

    @GetMapping("/books/{id}")
    public String libraryBook(Model model, @PathVariable long id) {
        Book book = libraryService.getBookById(id);

        if (book == null) {
            model.addAttribute("error", "Book not found");
            return "redirect:/library/books";
        }

        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping("/books")
    public String libraryBookAdd(Model model, @Valid @ModelAttribute Book book) {
        libraryService.addBook(book);
        return "redirect:/library/books";
    }

    @PutMapping("books/{id}")
    public String libraryBookUpdate(@Valid @ModelAttribute("book") Book book, @PathVariable long id) {
        Optional<Book> bookEntity = libraryService.updateBook(id, book);
        if (bookEntity.isPresent()) {
            return "redirect:/library/books";
        }
        return "error";
    }
}