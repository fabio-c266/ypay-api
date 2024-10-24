package com.ypay.api.services;

import com.ypay.api.domain.address.Address;
import com.ypay.api.domain.address.City;
import com.ypay.api.dtos.CreateAddressDTO;
import com.ypay.api.dtos.UpdateAddressDTO;
import com.ypay.api.infra.exceptions.DTOEmptyException;
import com.ypay.api.infra.exceptions.EntityNotFoundException;
import com.ypay.api.repositories.AddressRepository;
import com.ypay.api.repositories.CityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CityRepository cityRepository;

    public Address create(CreateAddressDTO createAddressDTO) throws EntityNotFoundException {
        City city = this.getCityByName(createAddressDTO.cityName());

        Address newAddress = new Address(
                null,
                createAddressDTO.cep(),
                createAddressDTO.street(),
                createAddressDTO.neighborhood(),
                createAddressDTO.complement(),
                createAddressDTO.number(),
                city
        );

        return this.addressRepository.save(newAddress);
    }

    @Transactional
    public Address update(Long id, UpdateAddressDTO updateAddressDTO) throws EntityNotFoundException, DTOEmptyException {
        Address address = this.getById(id);

        if (updateAddressDTO == null) throw new DTOEmptyException();
        if (updateAddressDTO.cep() != null) address.setCep(updateAddressDTO.cep());
        if (updateAddressDTO.street() != null) address.setStreet(updateAddressDTO.street());
        if (updateAddressDTO.neighborhood() != null) address.setNeighborhood(updateAddressDTO.neighborhood());
        if (updateAddressDTO.complement() != null) address.setComplement(updateAddressDTO.complement());
        if (updateAddressDTO.number()  > 0) address.setNumber(updateAddressDTO.number());

        if (updateAddressDTO.cityName() != null) {
            City city = this.getCityByName(updateAddressDTO.cityName());
            address.setCity(city);
        }

        return address;
    }

    public Address getById(Long id) throws EntityNotFoundException {
        return this.addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço"));
    }

    public City getCityByName(String name) throws EntityNotFoundException {
        return this.cityRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Cidade"));
    }
}
