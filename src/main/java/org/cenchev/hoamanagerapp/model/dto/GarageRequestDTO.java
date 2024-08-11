package org.cenchev.hoamanagerapp.model.dto;

import org.cenchev.hoamanagerapp.model.enums.SpaceType;

public class GarageRequestDTO {
    private SpaceType spaceType;
    private int count;

    public GarageRequestDTO() {
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
