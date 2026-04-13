package com.racecontrol.api.support.builders;

import com.racecontrol.api.domain.model.Contract;
import com.racecontrol.api.domain.model.Driver;
import com.racecontrol.api.domain.model.Season;
import com.racecontrol.api.domain.model.Team;

import java.util.UUID;

public class ContractBuilder {

    private UUID id = UUID.randomUUID();
    private Driver driver = DriverBuilder.driver().build();
    private Team team = TeamBuilder.team().build();
    private Season season = SeasonBuilder.season().build();
    private boolean primaryDriver;
    private boolean active;

    public static ContractBuilder contract() {
        return new ContractBuilder();
    }

    public ContractBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public ContractBuilder withDriver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public ContractBuilder withTeam(Team team) {
        this.team = team;
        return this;
    }

    public ContractBuilder withSeason(Season season) {
        this.season = season;
        return this;
    }

    public ContractBuilder withPrimaryDriver(boolean primaryDriver) {
        this.primaryDriver = primaryDriver;
        return this;
    }

    public ContractBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public Contract build() {
        return Contract.create(driver, team, season, primaryDriver);
    }
}
