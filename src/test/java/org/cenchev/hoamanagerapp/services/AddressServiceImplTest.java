package org.cenchev.hoamanagerapp.services;

import jakarta.persistence.EntityNotFoundException;
import org.cenchev.hoamanagerapp.model.dto.AddressDTO;
import org.cenchev.hoamanagerapp.model.entities.Address;
import org.cenchev.hoamanagerapp.repository.AddressRepository;
import org.cenchev.hoamanagerapp.services.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private AddressDTO addressDTO;
    private Address address;

    @BeforeEach
    public void setUp() {
        addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setAddressLine("123 First St");
        addressDTO.setCity("Anytown");
        addressDTO.setState("Anystate");
        addressDTO.setUsZipCode("12345");

        address = new Address();
        address.setId(1L);
        address.setAddressLine("123 First St");
        address.setCity("Anytown");
        address.setState("Anystate");
        address.setUsZipCode("12345");
    }

    @Test
    public void testSaveAddress() {
        // Check method if it saves the address correctly
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.saveAddress(addressDTO);
        assertEquals(address, result);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    public void testMapAddressDTOToAddress() {
        // check method if  it maps the DTO to an entity correctly
        Address result = addressService.mapAddressDTOToAddress(addressDTO);

        assertEquals(addressDTO.getId(), result.getId());
        assertEquals(addressDTO.getAddressLine(), result.getAddressLine());
        assertEquals(addressDTO.getCity(), result.getCity());
        assertEquals(addressDTO.getState(), result.getState());
        assertEquals(addressDTO.getUsZipCode(), result.getUsZipCode());
    }

    @Test
    public void testMapAddressToAddressDto() {
        // Check method if it maps the entity to a DTO correctly
        AddressDTO result = addressService.mapAddressToAddressDto(address);

        assertEquals(address.getId(), result.getId());
        assertEquals(address.getAddressLine(), result.getAddressLine());
        assertEquals(address.getCity(), result.getCity());
        assertEquals(address.getState(), result.getState());
        assertEquals(address.getUsZipCode(), result.getUsZipCode());
    }

    @Test
    public void testUpdateAddress_AddressExists() {
        // Check method when the address exists, ensuring it updates the address correctly
        when(addressRepository.findById(addressDTO.getId())).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.updateAddress(addressDTO);

        assertEquals(addressDTO.getAddressLine(), result.getAddressLine());
        assertEquals(addressDTO.getCity(), result.getCity());
        assertEquals(addressDTO.getState(), result.getState());
        assertEquals(addressDTO.getUsZipCode(), result.getUsZipCode());
        verify(addressRepository, times(1)).findById(addressDTO.getId());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    public void testUpdateAddress_AddressNotExists() {
        // Check method when the address does not exist, ensuring it throws an EntityNotFoundException.
        when(addressRepository.findById(addressDTO.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            addressService.updateAddress(addressDTO);
        });
        verify(addressRepository, times(1)).findById(addressDTO.getId());
        verify(addressRepository, never()).save(any(Address.class));
    }
}
