package org.cenchev.hoamanager.reservation.model.dto;

import org.cenchev.hoamanager.reservation.model.enums.SpaceType;

public class GarageDesignatedDTO {
    private SpaceType spaceType;
    private int count;

    public GarageDesignatedDTO() {
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
