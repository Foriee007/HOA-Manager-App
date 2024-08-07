package org.cenchev.hoamanager.reservation.model.dto;

import jakarta.persistence.ElementCollection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationInitiateDTO {

    private long homeId;
    private long clientId; // Added field for client ID
    private LocalDate startDate;
    private LocalDate endDate;
    private long days;
    @ElementCollection
    private List<GarageDesignatedDTO> garageDesignated = new ArrayList<>();
    private BigDecimal totalPrice;

    public ReservationInitiateDTO(long homeId, long clientId, LocalDate startDate, LocalDate endDate, long days, List<GarageDesignatedDTO> garageDesignated, BigDecimal totalPrice) {
        this.homeId = homeId;
        this.clientId = clientId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.garageDesignated = garageDesignated;
        this.totalPrice = totalPrice;
    }

    // Getters and setters
    public long getHomeId() {
        return homeId;
    }

    public void setHomeId(long homeId) {
        this.homeId = homeId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
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

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public List<GarageDesignatedDTO> getGarageDesignated() {
        return garageDesignated;
    }

    public void setGarageDesignated(List<GarageDesignatedDTO> garageDesignated) {
        this.garageDesignated = garageDesignated;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
