package com.racecontrol.api.season;

import com.racecontrol.api.league.League;
import com.racecontrol.api.league.LeagueBuilder;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

public class SeasonBuilder {

    private UUID id = UUID.randomUUID();
    private String title = "Season 2026";
    private LocalDate startDate = LocalDate.of(2026, Month.MAY, 1);
    private LocalDate endDate = LocalDate.of(2026, Month.MAY, 31);
    private SeasonStatus status = SeasonStatus.REGISTRATION_OPEN;
    private League league = LeagueBuilder.league().build();
    private Clock clock = Clock.systemDefaultZone();

    public static SeasonBuilder season() {
        return new SeasonBuilder();
    }

    public SeasonBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public SeasonBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public SeasonBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public SeasonBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public SeasonBuilder withStatus(SeasonStatus status) {
        this.status = status;
        return this;
    }

    public SeasonBuilder withLeague(League league) {
        this.league = league;
        return this;
    }

    public SeasonBuilder withClock(Clock clock) {
        this.clock = clock;
        return this;
    }

    public Season build() {
        return Season.create(title, startDate, endDate, league, clock);
    }

}
