package com.ypay.api.dtos;

import com.ypay.api.domain.user.User;
import com.ypay.api.domain.user.UserType;

import java.math.BigDecimal;

public record SimpleUserResponseDTO(
        String full_name,
        UserType userType,
        BigDecimal balance,
        String email,
        String cpf,
        String cnpj
) {
    public SimpleUserResponseDTO(User user) {
        this(
                user.getFullName(),
                user.getUserType(),
                user.getBalance(),
                user.getEmail(),
                user.getCpf(),
                user.getCnpj()
        );
    }
}
