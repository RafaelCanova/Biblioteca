package com.umc.librarian.controller;

import com.umc.librarian.service.BookService;
import com.umc.librarian.service.LoanService;
import com.umc.librarian.service.UserService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/loans")
@Validated
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final UserService userService;

    public LoanController(LoanService loanService, BookService bookService, UserService userService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping
    public String listLoans(Model model) {
        model.addAttribute("loans", loanService.findAll());
        return "loans/list";
    }

    @GetMapping("/new")
    public String newLoan(Model model) {
        model.addAttribute("books", bookService.findAvailableBooks());
        model.addAttribute("users", userService.findAll());
        return "loans/form";
    }

    @PostMapping
    public String saveLoan(@RequestParam @NotBlank String bookId,
                           @RequestParam @NotBlank String userId,
                           @RequestParam(defaultValue = "7") @Min(1) int days,
                           RedirectAttributes redirectAttributes) {
        loanService.createLoan(bookId, userId, days);
        redirectAttributes.addFlashAttribute("success", "Empréstimo registrado.");
        return "redirect:/loans";
    }

    @PostMapping("/return/{id}")
    public String returnLoan(@PathVariable @NonNull String id, RedirectAttributes redirectAttributes) {
        loanService.returnLoan(id);
        redirectAttributes.addFlashAttribute("success", "Devolução confirmada.");
        return "redirect:/loans";
    }
}