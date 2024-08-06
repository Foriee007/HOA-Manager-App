package org.cenchev.hoamanagerapp.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.cenchev.hoamanagerapp.model.dto.AddressDTO;
import org.cenchev.hoamanagerapp.model.entities.Address;
import org.cenchev.hoamanagerapp.repository.AddressRepository;
import org.cenchev.hoamanagerapp.services.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address saveAddress(AddressDTO addressDTO) {
        Address address = mapAddressDTOToAddress(addressDTO);

        return addressRepository.save(address);
    }

    @Override
    public Address mapAddressDTOToAddress(AddressDTO dto) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setAddressLine(dto.getAddressLine());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setUsZipCode(dto.getUsZipCode());
        return address;
    }

    @Override
    public AddressDTO mapAddressToAddressDto(Address address) {
        AddressDTO addressDTOs = new AddressDTO();
        addressDTOs.setId(address.getId());
        addressDTOs.setAddressLine(address.getAddressLine());
        addressDTOs.setCity(address.getCity());
        addressDTOs.setState(address.getState());
        addressDTOs.setUsZipCode(address.getUsZipCode());
        return addressDTOs;

    }
    //Check if home address  exist  before update instead doing "mapAddressDtoToAddress"
    @Override
    public Address updateAddress(AddressDTO addressDTO) {
        Address getExistingAddress = addressRepository.findById(addressDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Address not found linked to this home"));

        getExistingAddress.setAddressLine(addressDTO.getAddressLine());
        getExistingAddress.setCity(addressDTO.getCity());
        getExistingAddress.setState(addressDTO.getState());
        getExistingAddress.setUsZipCode(addressDTO.getUsZipCode());

        return addressRepository.save(getExistingAddress);
    }
}
