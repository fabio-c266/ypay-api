package com.ypay.api.dtos;

import com.ypay.api.domain.user.User;
import com.ypay.api.domain.user.UserType;

import java.math.BigDecimal;

public record UserResponseDTO(
        Long id,
        String full_name,
        UserType user_type,
        BigDecimal balance,
        String email,
        String phone,
        String cpf,
        String cnpj,
        boolean active,
        AddressResponseDTO address
) {
    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getFullName(),
                user.getUserType(),
                user.getBalance(),
                user.getEmail(),
                user.getPhone(),
                user.getCpf(),
                user.getCnpj(),
                user.isActive(),
                new AddressResponseDTO(user.getAddress())
        );
    }
}
