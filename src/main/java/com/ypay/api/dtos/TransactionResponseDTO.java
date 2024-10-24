package com.ypay.api.dtos;

import com.ypay.api.domain.transaction.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        BigDecimal amount,
        SimpleUserResponseDTO payer,
        SimpleUserResponseDTO receive,
        LocalDateTime created_at
) {
    public TransactionResponseDTO(Transaction transaction) {
        this(
                transaction.getId(),
                transaction.getAmount(),
                new SimpleUserResponseDTO(transaction.getPayer()),
                new SimpleUserResponseDTO(transaction.getReceive()),
                transaction.getCreatedAt()
        );
    }
}
