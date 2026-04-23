package com.umc.librarian.service;

import com.umc.librarian.model.Book;
import com.umc.librarian.model.Loan;
import com.umc.librarian.model.User;
import com.umc.librarian.repository.LoanRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;

    public LoanService(LoanRepository loanRepository, BookService bookService, UserService userService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public List<Loan> findOpenLoans() {
        return loanRepository.findByReturnedFalse();
    }

    public Loan createLoan(@NonNull String bookId, @NonNull String userId, int days) {
        if (days <= 0) throw new IllegalArgumentException("O prazo deve ser maior que zero.");

        Book book = bookService.findById(bookId);
        User user = userService.findById(userId);

        if (!user.isActive()) throw new IllegalStateException("Usuário inativo.");
        if (book.getAvailableQuantity() <= 0) throw new IllegalStateException("Livro indisponível.");

        Loan loan = new Loan();
        loan.setBookId(book.getId());
        loan.setBookTitle(book.getTitle());
        loan.setUserId(user.getId());
        loan.setUserName(user.getName());
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(days));
        loan.setReturned(false);

        bookService.decrementAvailable(bookId);
        return loanRepository.save(loan);
    }

    public void returnLoan(@NonNull String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));

        if (loan.isReturned()) throw new IllegalStateException("Já devolvido.");

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);
        
        bookService.incrementAvailable(Objects.requireNonNull(loan.getBookId()));
    }
}