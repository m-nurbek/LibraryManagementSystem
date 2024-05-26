package nurbek.librarymanagementsystem.controller;

import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    @GetMapping("/books")
    public String libraryBooks(Model model) {
        model.addAttribute("books", libraryService.getBookList());
        return "books";
    }
}