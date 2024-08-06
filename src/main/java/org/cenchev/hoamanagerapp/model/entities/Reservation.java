package org.cenchev.hoamanagerapp.model.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String confirmationNumber;

    @CreationTimestamp
    private LocalDateTime bookingDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Resident resident;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Home home;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservedParking> bookedParking = new ArrayList<>();

    @OneToOne(mappedBy = "reservation")
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        this.confirmationNumber = UUID.randomUUID().toString().substring(0, 8);
    }

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate checkinDate) {
        this.startDate = checkinDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate checkoutDate) {
        this.endDate = checkoutDate;
    }

    public List<ReservedParking> getBookedParking() {
        return bookedParking;
    }

    public void setBookedParking(List<ReservedParking> bookedParking) {
        this.bookedParking = bookedParking;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
