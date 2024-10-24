package com.ypay.api.controllers;

import com.ypay.api.domain.transaction.Transaction;
import com.ypay.api.dtos.CreateTransactionDTO;
import com.ypay.api.dtos.TransactionResponseDTO;
import com.ypay.api.infra.exceptions.EntityNotFoundException;
import com.ypay.api.infra.exceptions.ValidationException;
import com.ypay.api.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    private ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid CreateTransactionDTO createTransactionDTO) throws ValidationException, EntityNotFoundException {
        String payerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Transaction transaction = this.transactionService.transfer(payerEmail, createTransactionDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new TransactionResponseDTO(transaction)
        );
    }

    @GetMapping("/all")
    private ResponseEntity<Page<TransactionResponseDTO>> listAll(@PageableDefault(size = 6, sort = {"createdAt", "amount"}) Pageable pageable) throws EntityNotFoundException {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<Transaction> transactionsPage = this.transactionService.getAll(userEmail, pageable);
        Page<TransactionResponseDTO> formatted = transactionsPage.map(TransactionResponseDTO::new);

        return ResponseEntity.ok().body(formatted);
    }
}
