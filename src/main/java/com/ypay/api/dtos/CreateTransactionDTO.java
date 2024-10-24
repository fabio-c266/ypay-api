package com.ypay.api.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record CreateTransactionDTO(
        @NotNull(message = "campo vazio")
        @DecimalMin(value = "0.01", message = "o valor deve ser no mínimo 1 centavo")
        BigDecimal amount,

        @NotNull(message = "campo vazio")
        @Range(min = 1, message = "recebedor inválido")
        @JsonAlias("receive_id") Long receiveId
) {
}
