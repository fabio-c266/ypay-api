package com.ypay.api.services;

import com.ypay.api.domain.address.City;
import com.ypay.api.domain.address.UF;
import com.ypay.api.domain.user.User;
import com.ypay.api.domain.user.UserType;
import com.ypay.api.dtos.CreateAddressDTO;
import com.ypay.api.dtos.CreateUserDTO;
import com.ypay.api.dtos.UpdateAddressDTO;
import com.ypay.api.dtos.UpdateUserDTO;
import com.ypay.api.infra.exceptions.ConflictException;
import com.ypay.api.infra.exceptions.DTOEmptyException;
import com.ypay.api.infra.exceptions.EntityNotFoundException;
import com.ypay.api.infra.exceptions.ValidationException;
import com.ypay.api.repositories.CityRepository;
import com.ypay.api.repositories.UFRepository;
import com.ypay.api.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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
    public void saveCityToRegisterAddress() {
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
    }

    @AfterEach
    public void clearAllInfosToNextTest() {
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("Not be able create legal account with invalid CNPJ")
    public void shouldNotCreateLegalAccountUsingInvalidCNPJ() {
        CreateUserDTO userDTO = new CreateUserDTO(
                "Joe Doe",
                UserType.JURIDICA,
                "joedoe@gmail.com",
                "98965653434",
                null,
                "76713153000142",
                "password123",
                addressDTO
        );

        Assertions.assertThrows(ValidationException.class, () -> {
           this.userService.create(userDTO);
        });
    }

    @Test
    @DisplayName("Should be able create user using valid CNPJ")
    public void shouldBeCreateLegalAccount() throws ValidationException, ConflictException, EntityNotFoundException {
        CreateUserDTO userDTO = new CreateUserDTO(
                "Joe Doe",
                UserType.JURIDICA,
                "joedoe@gmail.com",
                "98965653434",
                null,
                "76713153000148",
                "password123",
                addressDTO
        );

        User userCreated = this.userService.create(userDTO);
        Assertions.assertNotNull(userCreated);
    }

    @Test
    @DisplayName("Should be not create physic account using invalid CPF")
    public void shouldNotBeCreatePhysicAccountUsingInvalidCPF() {
        CreateUserDTO userDTO = new CreateUserDTO(
                "Joe Doe",
                UserType.FISICA,
                "joedoe@gmail.com",
                "98965653434",
                "61434343211",
                null,
                "password123",
                addressDTO
        );

        Assertions.assertThrows(ValidationException.class, () -> {
            this.userService.create(userDTO);
        });
    }

    @Test
    @DisplayName("Should be create physic account using valid CPF")
    public void shouldBeCreatePhysicAccount() throws ValidationException, ConflictException, EntityNotFoundException {
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

        User userCreated = this.userService.create(userDTO);
        Assertions.assertNotNull(userCreated);
    }

    @Test
    @DisplayName("Should be update user infos")
    public void shouldBeUpdateUserInfos() throws ValidationException, ConflictException, EntityNotFoundException, DTOEmptyException {
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

        User userCreated = this.userService.create(userDTO);

        UpdateUserDTO updateUserDTO =  new UpdateUserDTO(
            "The best tester",
            "teste@gmail.com",
            "98987765443",
                null
        );

        User userUpdated = this.userService.update(
                userCreated.getEmail(),
                updateUserDTO
        );

        Assertions.assertNotEquals(userCreated, userUpdated);
    }

    @Test
    @DisplayName("Should be able toggle user status")
    public void shouldBeAbleToggleUserStatus() throws ValidationException, ConflictException, EntityNotFoundException {
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

        User userCreated = this.userService.create(userDTO);
        this.userService.toggleIsActive(userCreated.getEmail());

        User userUpdated = this.userService.getById(userCreated.getId());
        Assertions.assertFalse(userUpdated.isActive());
    }
}