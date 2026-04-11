package com.racecontrol.api.domain.model;

import com.racecontrol.api.core.exception.BusinessRuleException;
import com.racecontrol.api.domain.model.enums.CarCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "race")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDateTime raceDateTime;

    @Column(nullable = false)
    private Integer lobbyLeadTimeMinutes = 15;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarCategory carCategory;

    @Column(nullable = false)
    private String circuit;

    @ManyToOne(fetch = FetchType.LAZY)
    private Season season;

    public Race(String name, LocalDateTime raceDateTime, CarCategory carCategory, String circuit, Season season) {
        if (circuit == null || circuit.isEmpty()) {
            throw new BusinessRuleException("Circuit cannot be empty.");
        }
        if (carCategory == null) {
            throw new BusinessRuleException("Car category is mandatory for a race.");
        }
        if (season == null) {
            throw new BusinessRuleException("Season is mandatory for a race.");
        }

        this.name = validateName(name);
        validateRaceDate(raceDateTime, season);
        this.raceDateTime = raceDateTime;
        this.carCategory = carCategory;
        this.circuit = circuit;
        this.season = season;
    }

    public static Race create(String name, LocalDateTime raceDateTime, CarCategory carCategory, String circuit, Season season) {
        return new Race(name, raceDateTime, carCategory, circuit, season);
    }


    public void updateName(String newName) {
        String name = validateName(newName);
        if (isUnchanged(name, this.name)) return;
        this.name = name;
    }

    public void updateRaceDateTime(LocalDateTime newRaceDateTime) {
        if (newRaceDateTime == null) {
            throw new BusinessRuleException("Race date cannot be null.");
        }
        validateRaceDate(newRaceDateTime, this.season);
        if (isUnchanged(newRaceDateTime, this.raceDateTime)) return;
        this.raceDateTime = newRaceDateTime;
    }

    public LocalDateTime getLobbyAnnouncementTime() {
        return this.raceDateTime.minusMinutes(this.lobbyLeadTimeMinutes);
    }

    public void updateLobbyLeadTime(Integer newLeadTime) {
        if (newLeadTime == null || newLeadTime < 0) {
            throw new BusinessRuleException("Lobby lead time cannot be negative.");
        }
        if (isUnchanged(newLeadTime, this.lobbyLeadTimeMinutes)) return;
        this.lobbyLeadTimeMinutes = newLeadTime;
    }

    public void updateCarCategory(CarCategory newCarCategory) {
        if (newCarCategory == null) {
            throw new BusinessRuleException("Car category cannot be null.");
        }
        if (isUnchanged(newCarCategory, this.carCategory)) return;
        this.carCategory = newCarCategory;
    }

    public void updateCircuit(String newCircuit) {
        if (newCircuit == null || newCircuit.trim().isEmpty()) {
            throw new BusinessRuleException("Circuit cannot be empty.");
        }
        if (isUnchanged(newCircuit, this.circuit)) return;
        this.circuit = newCircuit;
    }


    private String validateName(String name) {
        if (name == null) {
            throw new BusinessRuleException("Race name cannot be null.");
        }
        String value = name.trim().replaceAll("\\s+", " ");
        if (value.isEmpty()) {
            throw new BusinessRuleException("Race name cannot be empty.");
        }
        if (value.trim().length() < 5) {
            throw new BusinessRuleException("Race name must be at least 5 characters long.");
        }
        return value;
    }

    private void validateRaceDate(LocalDateTime dateTime, Season season) {
        if (dateTime == null) {
            throw new BusinessRuleException("Race date cannot be null.");
        }
        LocalDate raceDate = dateTime.toLocalDate();
        if (raceDate.isBefore(season.getStartDate()) || raceDate.isAfter(season.getEndDate())) {
            throw new BusinessRuleException("Race date is outside the season interval.");
        }
    }

    private <T> boolean isUnchanged(T newValue, T currentValue) {
        return Objects.equals(newValue, currentValue);
    }

}
