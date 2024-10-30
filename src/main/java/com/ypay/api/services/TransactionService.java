package com.ypay.api.services;

import com.ypay.api.domain.transaction.Transaction;
import com.ypay.api.domain.user.User;
import com.ypay.api.dtos.CreateTransactionDTO;
import com.ypay.api.infra.exceptions.EntityNotFoundException;
import com.ypay.api.infra.exceptions.ValidationException;
import com.ypay.api.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Transaction transfer(String payerEmail, CreateTransactionDTO createTransactionDTO) throws EntityNotFoundException, ValidationException {
        User payer = this.userService.getByEmail(payerEmail);
        User receive = this.userService.getById(createTransactionDTO.receiveId());

        if (payer.getId().equals(receive.getId())) {
            throw new ValidationException("Você não pode transferir para si mesmo");
        }

        if (!receive.isActive()) {
            throw new ValidationException("Usuário inativo no sistema");
        }

        this.userService.removeAmount(payer.getEmail(), createTransactionDTO.amount());
        this.userService.addAmount(receive.getEmail(), createTransactionDTO.amount());

        Transaction newTransaction = new Transaction(
                null,
                createTransactionDTO.amount(),
                payer,
                receive,
                null
        );

        return this.transactionRepository.save(newTransaction);
    }

    public Page<Transaction> getAll(String userEmail, Pageable pageable) throws EntityNotFoundException {
        User user = this.userService.getByEmail(userEmail);
        return this.transactionRepository.findAllByUserId(user.getId(), pageable);
    }
}
