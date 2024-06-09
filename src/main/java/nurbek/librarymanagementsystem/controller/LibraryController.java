package nurbek.librarymanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.Account;
import nurbek.librarymanagementsystem.dto.Book;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.property.ConfigurationProperty;
import nurbek.librarymanagementsystem.service.AuthorService;
import nurbek.librarymanagementsystem.service.LibraryService;
import nurbek.librarymanagementsystem.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;
    private final UserService userService;
    private final AuthorService authorService;
    private final int pageSize;

    public LibraryController(LibraryService libraryService, UserService userService, AuthorService authorService, ConfigurationProperty props) {
        this.libraryService = libraryService;
        this.userService = userService;
        this.authorService = authorService;
        pageSize = props.getPageSize();
    }

    @GetMapping(path = {"/books"})
    public String libraryBooks(Model model, @PageableDefault(sort = {"title"}, size = 5) Pageable pageable,
                               @RequestParam(value = "keyword", required = false) String keyword, Principal principal) {
        Pageable pageProps = pageable;
        if (pageProps.getPageSize() <= 5) {
            pageProps = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
        }

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

        // find the user by email
        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("user", account);
        }

        return "books";
    }

    @Secured("ROLE_LIBRARIAN")
    @GetMapping("/books/{id}")
    public String libraryBook(Model model, @PathVariable("id") long id, Principal principal) {
        Book book = libraryService.getBookById(id).orElse(null);

        if (book == null) {
            model.addAttribute("error", "Book not found");
            return "redirect:/library/books";
        }

        model.addAttribute("book", book);
        model.addAttribute("statusList", Arrays.stream(BookStatus.values()).map(BookStatus::name).toList());

        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("user", account);
        }

        return "book";
    }

    @Secured("ROLE_LIBRARIAN")
    @GetMapping("/books/add")
    public String libraryBookAddView(Model model, Principal principal) {
        if (principal != null) {
            Account account = userService.getAccountByEmail(principal.getName()).orElse(null);
            model.addAttribute("user", account);
        }

        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "bookAdd";
    }

    // TODO: test this controller
    @Secured("ROLE_LIBRARIAN")
    @PostMapping("/books/add")
    public String libraryBookAdd(@Valid @ModelAttribute Book book, Errors errors) {
        if (errors.hasErrors()) {
            return "bookAdd";
        }

        libraryService.addBook(book);
        return "redirect:/library/books";
    }

    @Secured("ROLE_LIBRARIAN")
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

    // TODO: test libraryBookRestore controller
    @Secured("ROLE_LIBRARIAN")
    @PutMapping("/books/restore/{id}")
    public String libraryBookRestore(@PathVariable("id") long id) {
        Optional<Book> book = libraryService.restoreBook(id);
        if (book.isEmpty()) {
            return "error";
        }
        return "redirect:/library/books";
    }

    // TODO: test libraryBookArchive controller
    @Secured("ROLE_LIBRARIAN")
    @DeleteMapping("/books/archive/{id}")
    public String libraryBookArchive(@PathVariable("id") long id) {
        Optional<Book> book = libraryService.archiveBook(id);
        if (book.isEmpty()) {
            return "error";
        }
        return "redirect:/library/books";
    }
}