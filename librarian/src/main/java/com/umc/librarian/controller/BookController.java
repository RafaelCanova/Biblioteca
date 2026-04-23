package com.umc.librarian.controller;

import com.umc.librarian.model.Book;
import com.umc.librarian.service.BookService;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books/list";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/form";
    }

    @PostMapping
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "books/form";

        bookService.save(book);
        redirectAttributes.addFlashAttribute("success", "Obra salva no acervo Librarian.");
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable @NonNull String id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "books/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable @NonNull String id, RedirectAttributes redirectAttributes) {
        bookService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Obra removida com sucesso.");
        return "redirect:/books";
    }
}