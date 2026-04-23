package com.umc.librarian.controller;

import com.umc.librarian.service.BookService;
import com.umc.librarian.service.ReservationService;
import com.umc.librarian.service.UserService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservations")
@Validated
public class ReservationController {

    private final ReservationService reservationService;
    private final BookService bookService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, BookService bookService, UserService userService) {
        this.reservationService = reservationService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "reservations/list";
    }

    @GetMapping("/new")
    public String newReservation(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("users", userService.findAll());
        return "reservations/form";
    }

    @PostMapping
    public String saveReservation(@RequestParam @NotBlank String bookId,
                                  @RequestParam @NotBlank String userId,
                                  RedirectAttributes redirectAttributes) {
        reservationService.createReservation(bookId, userId);
        redirectAttributes.addFlashAttribute("success", "Fila de espera atualizada.");
        return "redirect:/reservations";
    }
}