package com.ypay.api.services;

import com.ypay.api.domain.address.City;
import com.ypay.api.domain.address.UF;
import com.ypay.api.domain.user.UserType;
import com.ypay.api.dtos.CreateAddressDTO;
import com.ypay.api.dtos.CreateUserDTO;
import com.ypay.api.dtos.LoginUserDTO;
import com.ypay.api.infra.exceptions.*;
import com.ypay.api.repositories.CityRepository;
import com.ypay.api.repositories.UFRepository;
import com.ypay.api.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UFRepository ufRepository;

    @Autowired
    private CityRepository cityRepository;

    private final CreateAddressDTO addressDTO = new CreateAddressDTO(
            "65060500",
            "Rua Z",
            "Dragonlandia",
            null,
            0,
            "São Paulo"
    );

    @BeforeAll
    public void registerUser() throws ValidationException, ConflictException, EntityNotFoundException {
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

        CreateUserDTO userDTO = new CreateUserDTO(
                "Joe Doe",
                UserType.FISICA,
                "joedoe@gmail.com",
                "98965653434",
                "57382871002",
                null,
                "password123",
                addressDTO
        );

        this.userService.create(userDTO);
    }

    @Test
    @DisplayName("Should be not authenticate user with invalid email")
    public void shouldBeNotAuthenticateWithInvalidEmail() {
        LoginUserDTO loginDTO = new LoginUserDTO(
                "jeaeae@gmail.com",
                "12345678"
        );

        Assertions.assertThrows(InvalidCredentialsException.class,() -> {
            this.authService.login(loginDTO);
        });
    }

    @Test
    @DisplayName("Should be not authenticate user with invalid password")
    public void shouldBeNotAuthenticateWithInvalidPassword() {
        LoginUserDTO loginDTO = new LoginUserDTO(
                "joedoe@gmail.com",
                "12345678"
        );

        Assertions.assertThrows(InvalidCredentialsException.class,() -> {
            this.authService.login(loginDTO);
        });
    }

    @Test
    @DisplayName("Should be authenticate user")
    public void shouldBeAuthenticateUser() throws InvalidCredentialsException, AuthException {
        LoginUserDTO loginDTO = new LoginUserDTO(
                "joedoe@gmail.com",
                "password123"
        );

        String token = this.authService.login(loginDTO);

        Assertions.assertNotNull(token);
    }
}