package com.racecontrol.api.builders;

import com.racecontrol.api.domain.model.Race;
import com.racecontrol.api.domain.model.Season;
import com.racecontrol.api.domain.model.enums.CarCategory;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

public class RaceBuilder {

    private UUID id = UUID.randomUUID();
    private String name = "Nürburgring Grand-Prix";
    private LocalDateTime raceTime = LocalDateTime.of(2026, Month.MAY, 10, 20, 0);
    private Integer timeMinutes;
    private CarCategory category = CarCategory.GR3;
    private String circuit = "Nürburgring";
    private Season season = SeasonBuilder.season().build();

    public static RaceBuilder race() {
        return new RaceBuilder();
    }

    public RaceBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public RaceBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RaceBuilder withRaceTime(LocalDateTime raceTime) {
        this.raceTime = raceTime;
        return this;
    }

    public RaceBuilder withLobbyLeadTimeMinutes(Integer timeMinutes) {
        this.timeMinutes = timeMinutes;
        return this;
    }

    public RaceBuilder withCategory(CarCategory category) {
        this.category = category;
        return this;
    }

    public RaceBuilder withCircuit(String circuit) {
        this.circuit = circuit;
        return this;
    }

    public RaceBuilder withSeason(Season season) {
        this.season = season;
        return this;
    }

    public Race build() {
        return Race.create(name, raceTime, category, circuit, season);
    }

}
