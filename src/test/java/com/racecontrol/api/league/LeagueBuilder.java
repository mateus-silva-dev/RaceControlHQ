package com.racecontrol.api.league;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class LeagueBuilder {

    private UUID id = UUID.randomUUID();
    private String name = "GT World Challenger 2026";
    private String description = "New regulations. New stories.";
    private String slugify = "gt-world-challenger-2026";
    private String logoUrl = null;
    private String rulesPdfUrl = null;
    private int pointsPole = 0;
    private int pointsFastestLap = 0;

    public static LeagueBuilder league() {
        return new LeagueBuilder();
    }

    public LeagueBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public LeagueBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LeagueBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public LeagueBuilder withSlugify(String slugify) {
        this.slugify = slugify;
        return this;
    }

    public LeagueBuilder withLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public LeagueBuilder withRulesPdfUrl(String rulesPdfUrl) {
        this.rulesPdfUrl = rulesPdfUrl;
        return this;
    }

    public LeagueBuilder withPointsPole(int pointsPole) {
        this.pointsPole = pointsPole;
        return this;
    }

    public LeagueBuilder withPointsFastestLap(int pointsFastestLap) {
        this.pointsFastestLap = pointsFastestLap;
        return this;
    }

    public League build() {
        League league = League.create(name, description);
        if (this.id != null) {
            ReflectionTestUtils.setField(league, "id", this.id);
        }
        if (this.slugify != null) {
            ReflectionTestUtils.setField(league, "slugify", this.slugify);
        }
        league.updateLogoUrl(this.logoUrl);
        league.updateRulesPdfUrl(this.rulesPdfUrl);

        return league;
    }

}
