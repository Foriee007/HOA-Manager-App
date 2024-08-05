package org.cenchev.hoamanagerapp.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.cenchev.hoamanagerapp.model.enums.SpaceType;

public class GarageDTO {
    private Long id;

    private Long homeId;

    private SpaceType spaceType;

    @NotNull(message = "parking spots count cannot be empty")
    @PositiveOrZero(message = "parking spots count must be 0 or more")
    private Integer parkingSpotCount;

    @NotNull(message = "Price cannot be empty")
    @PositiveOrZero(message = "Price per night must be 0 or more")
    private Double pricePerDay;

    //private MultipartFile garagePhotoUrl; //move to HomeRegistrationDTO


    public GarageDTO() {
    }

    public GarageDTO(Long id, Long homeId, SpaceType spaceType, Integer parkingSpotCount, Double pricePerDay) {
        this.id = id;
        this.homeId = homeId;
        this.spaceType = spaceType;
        this.parkingSpotCount = parkingSpotCount;
        this.pricePerDay = pricePerDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
    }

    public Integer getParkingSpotCount() {
        return parkingSpotCount;
    }

    public void setParkingSpotCount(Integer parkingSpotCount) {
        this.parkingSpotCount = parkingSpotCount;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
