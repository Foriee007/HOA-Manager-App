package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.model.dto.AddressDTO;
import org.cenchev.hoamanagerapp.model.entities.Address;

public interface AddressService {
    Address saveAddress(AddressDTO addressDTO);
    Address mapAddressDTOToAddress(AddressDTO dto);

    AddressDTO mapAddressToAddressDto(Address address);

    Address updateAddress(AddressDTO addressDTO);
}
