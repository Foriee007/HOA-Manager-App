package org.cenchev.hoamanagerapp.model.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "homes")
public class Home implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Garage> spot = new ArrayList<>();

    @OneToMany(mappedBy = "home")
    private List<Reservation> reservations= new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    private PropertyManager propertyManager;

    public Home() {
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Garage> getSpot() {
        return spot;
    }

    public void setSpot(List<Garage> spot) {
        this.spot = spot;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }


    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }
}
