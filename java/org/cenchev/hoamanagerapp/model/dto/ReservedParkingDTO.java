package org.cenchev.hoamanagerapp.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.cenchev.hoamanagerapp.model.enums.SpaceType;

public class ReservedParkingDTO {
    private Long id;

    private Long garageId;

    private SpaceType spaceType;

    @NotNull(message = "Enter available car parking spot")
    @PositiveOrZero(message = "Must have least 1 car parking ")
    private Integer parkingSpaceCount;

    @NotNull(message = "Price must be entered")
    @PositiveOrZero(message = "Daly parking rate cannot be 0")
    private Double dayRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGarageId() {
        return garageId;
    }

    public void setGarageId(Long garageId) {
        this.garageId = garageId;
    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
    }

    public Integer getParkingSpaceCount() {
        return parkingSpaceCount;
    }

    public void setParkingSpaceCount(Integer parkingSpaceCount) {
        this.parkingSpaceCount = parkingSpaceCount;
    }

    public Double getDayRate() {
        return dayRate;
    }

    public void setDayRate(Double dayRate) {
        this.dayRate = dayRate;
    }
}
