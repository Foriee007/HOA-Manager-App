package org.cenchev.hoamanagerapp.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

public class HomeDTO {
    private Long id;

    @NotBlank(message = "Property name cannot be empty")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z0-9 ]+$", message = "Property name must only contain letters and numbers")
    private String name;

    @Valid
    private AddressDTO addressDTO;

    @Valid
    private List<GarageDTO> garageDTOS = new ArrayList<>();

    private String managerUsername;


    private String imageURL;

    public Long getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }
}
