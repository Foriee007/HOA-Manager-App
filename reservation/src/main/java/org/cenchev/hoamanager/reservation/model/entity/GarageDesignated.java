package org.cenchev.hoamanager.reservation.model.entity;

import jakarta.persistence.*;
import org.cenchev.hoamanager.reservation.model.enums.SpaceType;

@Embeddable
public class GarageDesignated {

    @Enumerated(EnumType.STRING)
    private SpaceType spaceType;
    private int count;

    public GarageDesignated(SpaceType spaceType, int count) {
        this.spaceType = spaceType;
        this.count = count;
    }

    public GarageDesignated() {

    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
