package com.umc.librarian.service;

import com.umc.librarian.model.Book;
import com.umc.librarian.model.Reservation;
import com.umc.librarian.model.User;
import com.umc.librarian.repository.ReservationRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookService bookService;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository, BookService bookService, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation createReservation(@NonNull String bookId, @NonNull String userId) {
        Book book = bookService.findById(bookId);
        User user = userService.findById(userId);

        if (!user.isActive()) throw new IllegalStateException("Usuário inativo.");
        if (book.getAvailableQuantity() > 0) {
            throw new IllegalStateException("Livro disponível. Faça um empréstimo.");
        }

        if (reservationRepository.existsByBookIdAndUserIdAndStatus(bookId, userId, "ATIVA")) {
            throw new IllegalStateException("Já possui uma reserva ativa para este livro.");
        }

        Reservation reservation = new Reservation();
        reservation.setBookId(book.getId());
        reservation.setBookTitle(book.getTitle());
        reservation.setUserId(user.getId());
        reservation.setUserName(user.getName());
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("ATIVA");

        return reservationRepository.save(reservation);
    }
}