package org.cenchev.hoamanagerapp.model.dto;

import java.util.ArrayList;
import java.util.List;

public class AvailableHomeParkingDTO {
    private Long id;

    private String name;

    private AddressDTO addressDTO;

    private List<GarageDTO> garageDTOList = new ArrayList<>();

    private Integer maxAvailableEVSpots;
    private Integer maxAvailableAllDaySpots;
    private String imageUrl;

    public AvailableHomeParkingDTO() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
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

    public List<GarageDTO> getGarageDTOList() {
        return garageDTOList;
    }

    public void setGarageDTOList(List<GarageDTO> garageDTOList) {
        this.garageDTOList = garageDTOList;
    }

    public Integer getMaxAvailableEVSpots() {
        return maxAvailableEVSpots;
    }

    public void setMaxAvailableEVSpots(Integer maxAvailableEVSpots) {
        this.maxAvailableEVSpots = maxAvailableEVSpots;
    }

    public Integer getMaxAvailableAllDaySpots() {
        return maxAvailableAllDaySpots;
    }

    public void setMaxAvailableAllDaySpots(Integer maxAvailableAllDaySpots) {
        this.maxAvailableAllDaySpots = maxAvailableAllDaySpots;
    }
}
