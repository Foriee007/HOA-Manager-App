package org.cenchev.hoamanagerapp.model.entities;

import jakarta.persistence.*;
import org.cenchev.hoamanagerapp.model.enums.SpaceType;

@Entity
@Table(name = "reservedspots")
public class ReservedParking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceType spaceType;

    @Column(nullable = false)
    private int count;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Reservation reservation;

    public ReservedParking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public void setspaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
