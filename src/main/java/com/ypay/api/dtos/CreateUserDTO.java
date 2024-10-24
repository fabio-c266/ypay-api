package com.ypay.api.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ypay.api.domain.user.UserType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record CreateUserDTO(
        @NotBlank(message = "campo vazio")
        @Pattern(regexp = "^(?=.{1,100}$)\\S{3,}(?:\\s+\\S+)+$", message = "formato de nome completo inválido")
        @JsonAlias("full_name") String fullName,

        @NotNull(message = "campo vazio")
        @JsonAlias("user_type") UserType userType,

        @NotBlank(message = "campo vazio")
        @Email(message = "email inválido")
        @Size(max = 80, message = "email muito grande")
        String email,

        @NotBlank(message = "campo vazio")
        @Size(min = 11, max = 11, message = "telefone inválido")
        String phone,

        @Size(min = 11, max = 11, message = "cpf inválido")
        String cpf,

        @Size(min = 14, max = 20, message = "cnpj inválido")
        String cnpj,

        @NotBlank(message = "campo vazio")
        @Size(min = 6, max = 80, message = "A senha deve ter no minimo 6 caracteris e no máximo 80")
        String password,

        @NotNull(message = "campo obrigatório")
        @JsonAlias("address") @Valid CreateAddressDTO createAddressDTO
        )
{
}
