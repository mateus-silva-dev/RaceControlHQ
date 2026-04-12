package com.racecontrol.api.support.builders;

import com.racecontrol.api.domain.model.Driver;
import com.racecontrol.api.domain.model.valueObject.Nationality;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

public class DriverBuilder {

    private UUID id = UUID.randomUUID();
    private String name = "Lewis Hamilton";
    private LocalDate birthDate = LocalDate.of(1999, Month.JUNE, 10);
    private Nationality nationality = new Nationality("GB");
    private String gamerTag = "xX_LewisHamilton_Xx";
    private boolean active;
    private Clock clock = Clock.systemDefaultZone();

    public static DriverBuilder driver() {
        return new DriverBuilder();
    }

    public DriverBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public DriverBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DriverBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public DriverBuilder withNationality(Nationality nationality) {
        this.nationality = nationality;
        return this;
    }

    public DriverBuilder withGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
        return this;
    }

    public DriverBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public DriverBuilder withClock(Clock clock) {
        this.clock = clock;
        return this;
    }

    public Driver build() {
        return Driver.create(name, birthDate, nationality, gamerTag, clock);
    }

}
