package org.cenchev.hoamanagerapp.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRequestDTO {
    private long homeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private long durationDays;
    private List<GarageRequestDTO> garageRequestDTOS = new ArrayList<>();
    private BigDecimal totalPrice;

    public ReservationRequestDTO() {
    }

    public long getHomeId() {
        return homeId;
    }

    public void setHomeId(long homeId) {
        this.homeId = homeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(long durationDays) {
        this.durationDays = durationDays;
    }

    public List<GarageRequestDTO> getGarageRequestDTOS() {
        return garageRequestDTOS;
    }

    public void setGarageRequestDTOS(List<GarageRequestDTO> garageRequestDTOS) {
        this.garageRequestDTOS = garageRequestDTOS;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
