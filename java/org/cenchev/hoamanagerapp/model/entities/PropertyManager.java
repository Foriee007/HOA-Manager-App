package org.cenchev.hoamanagerapp.model.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "managers")
public class PropertyManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(mappedBy = "propertyManager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Home> homelList = new ArrayList<>();

    public PropertyManager() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Home> getHomelList() {
        return homelList;
    }

    public void setHomelList(List<Home> homelList) {
        this.homelList = homelList;
    }
}
