package com.ypay.api.services;

import com.ypay.api.domain.address.City;
import com.ypay.api.domain.address.UF;
import com.ypay.api.domain.transaction.Transaction;
import com.ypay.api.domain.user.User;
import com.ypay.api.domain.user.UserType;
import com.ypay.api.dtos.CreateAddressDTO;
import com.ypay.api.dtos.CreateTransactionDTO;
import com.ypay.api.dtos.CreateUserDTO;
import com.ypay.api.infra.exceptions.ConflictException;
import com.ypay.api.infra.exceptions.EntityNotFoundException;
import com.ypay.api.infra.exceptions.ValidationException;
import com.ypay.api.repositories.CityRepository;
import com.ypay.api.repositories.UFRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTest {
    @Autowired
    private UFRepository ufRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    User payer = null;
    User receive = null;

    CreateTransactionDTO transactionDTO = null;

    @BeforeAll
    public void createUsers() throws ValidationException, ConflictException, EntityNotFoundException {
        UF uf = new UF(
                null,
                "São Paulo",
                "SP"
        );

        City city = new City(
                null,
                "São Paulo",
                this.ufRepository.save(uf)
        );

        this.cityRepository.save(city);

        CreateAddressDTO addressDTO = new CreateAddressDTO(
                "13508971",
                "Rua Z",
                "Super rua",
                null,
                0,
                "São Paulo"
        );

        CreateUserDTO userDTO1 = new CreateUserDTO(
                "Joe Doe",
                UserType.FISICA,
                "joedoe@gmail.com",
                "98976654321",
                "81134056079",
                null,
                "password123",
                addressDTO
        );

        CreateUserDTO userDTO2 = new CreateUserDTO(
                "ELon ELay",
                UserType.FISICA,
                "elonelay@gmail.com",
                "98976654323",
                "25605850067",
                null,
                "password123",
                addressDTO
        );

        payer = this.userService.create(userDTO1);
        receive = this.userService.create(userDTO2);

        transactionDTO = new CreateTransactionDTO(
                new BigDecimal("20.00"),
                receive.getId()
        );
    }

    @Test
    public void shouldBeNotTransferWithMissingBalance() {
        Assertions.assertThrows(ValidationException.class, () -> {
            this.transactionService.transfer(payer.getEmail(), transactionDTO);
        });
    }

    @Test
    public void shouldBeNotTransferToSameUser() {
        Assertions.assertThrows(ValidationException.class, () -> {
            this.transactionService.transfer(payer.getEmail(), transactionDTO);
        });
    }

    @Test
    public void shouldBeNotTransferToInactiveUser() throws EntityNotFoundException {
        this.userService.toggleIsActive(receive.getEmail());

        Assertions.assertThrows(ValidationException.class, () -> {
            this.transactionService.transfer(payer.getEmail(), transactionDTO);
        });
    }

    @Test
    public void shouldBeTransferAmountToUser() throws ValidationException, EntityNotFoundException {
        this.userService.toggleIsActive(receive.getEmail());

        this.userService.addAmount(
                payer.getEmail(),
                new BigDecimal("30.00")
        );

        Transaction newTransaction = transactionService.transfer(payer.getEmail(), transactionDTO);
        Assertions.assertNotNull(newTransaction);

        User payerUpdated = userService.getById(payer.getId());
        User receiveUpdated = userService.getById(receive.getId());

        Assertions.assertEquals(new BigDecimal("10.00"), payerUpdated.getBalance());
        Assertions.assertEquals(new BigDecimal("20.00"), receiveUpdated.getBalance());
    }
}