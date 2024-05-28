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
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/books")
    public String libraryBooks(Model model, @PageableDefault(sort = { "title" }, value = 10) Pageable pageable) {
        Page<BookEntity> bookList = libraryService.getBookList(pageable);
        model.addAttribute("books", bookList);

        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", bookList.getTotalPages());
        model.addAttribute("totalItems", bookList.getTotalElements());

        return "books";
    }
}