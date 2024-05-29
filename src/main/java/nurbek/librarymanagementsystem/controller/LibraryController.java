package nurbek.librarymanagementsystem.controller;

import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
        Page<BookEntity> bookList;
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
    public String libraryBook(Model model, @PathVariable int id) {
        BookEntity book = libraryService.getBookById(id);

        if (book == null) {
            model.addAttribute("error", "Book not found");
            return "redirect:/library/books";
        }

        model.addAttribute("book", book);
        return "book";
    }
}