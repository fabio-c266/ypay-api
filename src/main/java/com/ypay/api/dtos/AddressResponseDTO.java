package com.ypay.api.dtos;

import com.ypay.api.domain.address.Address;

public record AddressResponseDTO(
        Long id,
        String cep,
        String street,
        String neighborhood,
        String complement,
        int number,
        String city_name,
        String uf
) {
    public AddressResponseDTO(Address address) {
        this(
                address.getId(),
                address.getCep(),
                address.getStreet(),
                address.getNeighborhood(),
                address.getComplement(),
                address.getNumber(),
                address.getCity().getName(),
                address.getCity().getUf().getName()
        );
    }
}
