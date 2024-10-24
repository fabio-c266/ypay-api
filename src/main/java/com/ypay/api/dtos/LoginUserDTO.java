package com.ypay.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserDTO(
        @NotBlank(message = "campo vazio")
        @Email(message = "email inválido")
        @Size(max = 80, message = "email muito grande")
        String email,

        @NotBlank(message = "campo vazio")
        @Size(min = 6, max = 80, message = "senha inválida")
        String password
) {
}
