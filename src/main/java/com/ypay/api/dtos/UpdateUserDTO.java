package com.ypay.api.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @Pattern(regexp = "^(?=.{1,100}$)\\S{3,}(?:\\s+\\S+)+$", message = "formato de nome completo inválido")
        @JsonAlias("full_name") String fullName,

        @Email(message = "email inválido")
        @Size(max = 80, message = "email muito grande")
        String email,

        @NotBlank(message = "campo vazio")
        @Size(min = 11, max = 11, message = "telefone inválido")
        String phone,

        @JsonAlias("address") @Valid UpdateAddressDTO updateAddressDTO
)
{
}
