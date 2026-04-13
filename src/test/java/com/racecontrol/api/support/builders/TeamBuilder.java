package com.racecontrol.api.support.builders;

import com.racecontrol.api.domain.model.Team;
import com.racecontrol.api.domain.valueObject.HexColor;

import java.util.UUID;

public class TeamBuilder {

    private UUID id = UUID.randomUUID();
    private String name = "Mercedes-AMG Motorsports";
    private HexColor hexColor = new HexColor("#00A19B");
    private String carManufacturer = "Mercedes-AMG GT3";
    private String logoUrl;
    private boolean active;

    public static TeamBuilder team() {
        return new TeamBuilder();
    }

    public TeamBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TeamBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TeamBuilder withHexColor(HexColor hexColor) {
        this.hexColor = hexColor;
        return this;
    }

    public TeamBuilder withCarManufacturer(String carManufacturer) {
        this.carManufacturer = carManufacturer;
        return this;
    }

    public TeamBuilder withLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public TeamBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public Team build() {
        return Team.create(name, hexColor, carManufacturer);
    }
}
