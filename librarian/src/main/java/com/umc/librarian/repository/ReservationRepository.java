package com.umc.librarian.repository;

import com.umc.librarian.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByStatus(String status);
    boolean existsByBookIdAndUserIdAndStatus(String bookId, String userId, String status);
}