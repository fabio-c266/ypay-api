package com.ypay.api.repositories;

import com.ypay.api.domain.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(
            """
              SELECT t FROM Transaction t
              WHERE t.payer.id = :userId OR t.receive.id = :userId
            """
    )
    Page<Transaction> findAllByUserId(Long userId, Pageable pageable);
}
