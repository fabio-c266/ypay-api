package com.ypay.api.services;

import com.ypay.api.domain.address.Address;
import com.ypay.api.domain.user.User;
import com.ypay.api.domain.user.UserType;
import com.ypay.api.dtos.CreateUserDTO;
import com.ypay.api.dtos.UpdateUserDTO;
import com.ypay.api.infra.exceptions.ConflictException;
import com.ypay.api.infra.exceptions.DTOEmptyException;
import com.ypay.api.infra.exceptions.EntityNotFoundException;
import com.ypay.api.infra.exceptions.ValidationException;
import com.ypay.api.infra.security.SecurityConfigs;
import com.ypay.api.repositories.UserRepository;
import com.ypay.api.validations.CNPJValidation;
import com.ypay.api.validations.CPFValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;

    public User create(CreateUserDTO createUserDTO) throws ValidationException, ConflictException, EntityNotFoundException {
        if (createUserDTO.userType().equals(UserType.JURIDICA)) {
            if (createUserDTO.cnpj() == null) {
                throw new ValidationException("A criação de conta para pessoa juridica é obrigatório o CNPJ");
            }

            if (!CNPJValidation.isValid(createUserDTO.cnpj())) {
                throw new ValidationException("CNPJ inválido");
            }

            if (this.userRepository.existsByCnpj(createUserDTO.cnpj())) {
                throw new ConflictException("CNPJ");
            }
        } else {
            if (createUserDTO.cpf() == null) {
                throw new ValidationException("A criação de conta para pessoa física é obrigatório o CPF");
            }

            if (!CPFValidation.isValid(createUserDTO.cpf())) {
                throw new ValidationException("CPF inválido");
            }

            if (this.userRepository.existsByCpf(createUserDTO.cpf())) {
                throw new ConflictException("CPF");
            }
        }

        if (this.userRepository.existsByEmail(createUserDTO.email())) {
            throw new ConflictException("email");
        }

        if (this.userRepository.existsByPhone(createUserDTO.phone())) {
            throw new ConflictException("telefone");
        }

        Address address = this.addressService.create(createUserDTO.createAddressDTO());
        String passwordHashed = new BCryptPasswordEncoder().encode(createUserDTO.password());
        User newUser = new User(
                null,
                createUserDTO.fullName(),
                createUserDTO.userType(),
                new BigDecimal("0.00"),
                createUserDTO.email(),
                createUserDTO.phone(),
                createUserDTO.cpf(),
                createUserDTO.cnpj(),
                passwordHashed,
                true,
                address,
                null
        );

        return this.userRepository.save(newUser);
    }

    @Transactional
    public User update(String userEmail, UpdateUserDTO updateUserDTO) throws DTOEmptyException, EntityNotFoundException, ConflictException {
        if (updateUserDTO == null) {
            throw new DTOEmptyException();
        }

        User user = this.getByEmail(userEmail);

        if (updateUserDTO.fullName() != null) {
            user.setFullName(updateUserDTO.fullName());
        }

        if (updateUserDTO.email() != null) {
            if (this.userRepository.existsByEmail(updateUserDTO.email())) {
                throw new ConflictException("email");
            }

            user.setEmail(updateUserDTO.email());
        }

        if (updateUserDTO.phone() != null) {
            if (this.userRepository.existsByPhone(updateUserDTO.phone())) {
                throw new ConflictException("telefone");
            }

            user.setPhone(updateUserDTO.phone());
        }

        if (updateUserDTO.updateAddressDTO() != null) {
            Address addressUpdated = this.addressService.update(user.getAddress().getId(), updateUserDTO.updateAddressDTO());
            user.setAddress(addressUpdated);
        }

        return user;
    }

    @Transactional
    public void toggleIsActive(String userEmail) throws EntityNotFoundException {
        User user = this.getByEmail(userEmail);
        user.setActive(!user.isActive());
    }

    public User getByEmail(String email) throws EntityNotFoundException {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário"));
    }

    public User getById(Long id) throws EntityNotFoundException {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário"));
    }
}
