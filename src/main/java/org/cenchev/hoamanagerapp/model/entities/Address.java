package org.cenchev.hoamanagerapp.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String addressLine;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String usZipCode;

    @Column(nullable = false)
    private String State;

    public Address() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUsZipCode() {
        return usZipCode;
    }

    public void setUsZipCode(String zipcode) {
        this.usZipCode = zipcode;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
