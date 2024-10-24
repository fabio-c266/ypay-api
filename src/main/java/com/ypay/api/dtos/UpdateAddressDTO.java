package com.ypay.api.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Size;

public record UpdateAddressDTO(
        @Size(min = 8, max = 8, message = "CEP inválido")
        String cep,

        @Size(min = 3, max = 100, message = "no minimo 3 caracteris e no máximo 100")
        String street,

        @Size(min = 3, max = 100, message = "no minimo 3 caracteris e no máximo 100")
        String neighborhood,

        @Size(min = 3, max = 80, message = "no minimo 3 caracteris e no máximo 80")
        String complement,

        int number,

        @Size(min = 3, message = "cidade inválida")
        @JsonAlias("city_name") String cityName
) {
}
