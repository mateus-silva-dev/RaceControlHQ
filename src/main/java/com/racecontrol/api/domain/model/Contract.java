package com.racecontrol.api.domain.model;

import com.racecontrol.api.core.exception.BusinessRuleException;
import com.racecontrol.api.domain.model.enums.SeasonStatus;
import com.racecontrol.api.domain.validation.CommonValidation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "contract", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"driver_id", "team_id", "season_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(optional = false)
    @JoinColumn(name = "season_id")
    private Season season;

    private boolean primaryDriver;

    @Column(nullable = false)
    private boolean active;

    public Contract(Driver driver, Team team, Season season, boolean primaryDriver) {
        this.driver = CommonValidation.required(driver, "Driver");
        this.team = CommonValidation.required(team, "Team");
        this.season = CommonValidation.required(season, "Season");
        validateStatusSeason(season);
        this.primaryDriver = primaryDriver;
        this.active = true;
    }

    public static Contract create(Driver driver, Team team, Season season, boolean primaryDriver) {
        return new Contract(driver, team, season, primaryDriver);
    }


    public boolean isFromSeason(UUID seasonId) {
        return Objects.equals(this.season.getId(), seasonId);
    }

    public void promoteToPrimary() {
        if (this.primaryDriver) {
            throw new BusinessRuleException("Driver is already a primary driver.");
        }
        this.primaryDriver = true;
    }

    public void terminate() {
        this.active = false;
    }


    private void validateStatusSeason(Season season) {
        if (season.getStatus().equals(SeasonStatus.FINISHED)) {
            throw new BusinessRuleException("Cannot create contracts for a finished season.");
        }
    }
}
