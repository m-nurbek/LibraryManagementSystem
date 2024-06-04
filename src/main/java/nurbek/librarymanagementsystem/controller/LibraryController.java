package nurbek.librarymanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.property.ConfigurationProperty;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Slf4j
@Controller
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;
    private final int pageSize;

    public LibraryController(LibraryService libraryService, ConfigurationProperty props) {
        this.libraryService = libraryService;
        pageSize = props.getPageSize();
    }

    @GetMapping(path = {"/books"})
    public String libraryBooks(Model model, @PageableDefault(sort = {"title"}) Pageable pageable,
                               @RequestParam(value = "keyword", required = false) String keyword) {
        Pageable pageProps = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());

        Page<Book> bookList;
        if (keyword != null && !keyword.isEmpty()) {
            bookList = libraryService.searchBooksByKeyword(keyword, pageProps);
            model.addAttribute("books", bookList);
        } else {
            bookList = libraryService.getBookList(pageProps);
            model.addAttribute("books", bookList);
        }
        model.addAttribute("currentPage", pageProps.getPageNumber());
        model.addAttribute("totalPages", bookList.getTotalPages());
        model.addAttribute("totalItems", bookList.getTotalElements());

        return "books";
    }

    @GetMapping("/books/{id}")
    public String libraryBook(Model model, @PathVariable("id") long id) {
        Book book = libraryService.getBookById(id);

        if (book == null) {
            model.addAttribute("error", "Book not found");
            return "redirect:/library/books";
        }

        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping("/books")
    public String libraryBookAdd(Model model, @Valid @ModelAttribute Book book, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("error", "Invalid input");
        }
        libraryService.addBook(book);
        return "redirect:/library/books";
    }

    @PutMapping("/books/{id}")
    public String libraryBookUpdate(@PathVariable("id") long id, @Valid @ModelAttribute("book") Book book, Errors errors) {
        if (errors.hasErrors()) {
            return "error";
        }
        Optional<Book> bookEntity = libraryService.updateBook(id, book);
        if (bookEntity.isPresent()) {
            return "redirect:/library/books";
        }
        return "error";
    }
}