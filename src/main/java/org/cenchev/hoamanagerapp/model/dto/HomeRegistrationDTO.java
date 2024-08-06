package org.cenchev.hoamanagerapp.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class HomeRegistrationDTO {
    @NotBlank(message = "Home name cannot be empty")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z0-9 ]+$", message = "Home name must only contain letters and numbers")
    private String name;

    @Valid
    private AddressDTO addressDTO;

    @Valid
    private List<GarageDTO> garageDTOS = new ArrayList<>();

    @Valid
    private MultipartFile garagePhotoUrl;

    public HomeRegistrationDTO(String name, AddressDTO addressDTO, List<GarageDTO> garageDTOS, MultipartFile garagePhotoUrl) {
        this.name = name;
        this.addressDTO = addressDTO;
        this.garageDTOS = garageDTOS;
        this.garagePhotoUrl = garagePhotoUrl;
    }

    public HomeRegistrationDTO() {
    }

    public MultipartFile getGaragePhotoUrl() {
        return garagePhotoUrl;
    }

    public void setGaragePhotoUrl(MultipartFile garagePhotoUrl) {
        this.garagePhotoUrl = garagePhotoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
    }

    public List<GarageDTO> getGarageDTOS() {
        return garageDTOS;
    }

    public void setGarageDTOS(List<GarageDTO> garageDTOS) {
        this.garageDTOS = garageDTOS;
    }
}
