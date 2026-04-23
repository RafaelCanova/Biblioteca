package com.umc.librarian.repository;

import com.umc.librarian.model.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {
    List<Loan> findByReturnedFalse();
}