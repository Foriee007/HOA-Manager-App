package org.cenchev.hoamanager.reservation.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long homeId;
    private long clientId;
    private LocalDate startDate;
    private LocalDate endDate;
    private long days;
    private BigDecimal totalPrice;

    @ElementCollection
    private List<GarageDesignated> garageDesignated;

    public Reservation() {
    }

    public List<GarageDesignated> getGarageDesignated() {
        return garageDesignated;
    }

    public void setGarageDesignated(List<GarageDesignated> garageDesignated) {
        this.garageDesignated = garageDesignated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

   }
