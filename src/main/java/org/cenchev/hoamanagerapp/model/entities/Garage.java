package org.cenchev.hoamanagerapp.model.entities;

import jakarta.persistence.*;
import org.cenchev.hoamanagerapp.model.enums.SpaceType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "garages")
public class Garage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Home home;

    @Enumerated(EnumType.STRING)
    private SpaceType spaceType;

    private int parkingCount;

    private double dayUsePrice;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilityList = new ArrayList<>();

    public Garage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
    }

    public int getParkingCount() {
        return parkingCount;
    }

    public void setParkingCount(int parkingCount) {
        this.parkingCount = parkingCount;
    }

    public double getDayUsePrice() {
        return dayUsePrice;
    }

    public void setDayUsePrice(double dayUsePrice) {
        this.dayUsePrice = dayUsePrice;
    }

    public List<Availability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailabilityList(List<Availability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
