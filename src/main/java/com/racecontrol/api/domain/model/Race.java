package com.racecontrol.api.domain.model;

import com.racecontrol.api.core.exception.BusinessRuleException;
import com.racecontrol.api.domain.model.enums.CarCategory;
import com.racecontrol.api.domain.validation.CommonValidation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "race")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "id")
public class Race extends BaseEntity {

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
        this.name = CommonValidation.requiredText(name, "Race name", 5);
        this.raceDateTime = CommonValidation.required(raceDateTime, "Race date");
        this.carCategory = CommonValidation.required(carCategory, "Car category");
        this.circuit = CommonValidation.requiredText(circuit, "Circuit", 5);
        this.season = CommonValidation.required(season, "Season");;
        validateRaceDate(raceDateTime, season);
    }

    public static Race create(String name, LocalDateTime raceDateTime, CarCategory carCategory, String circuit, Season season) {
        return new Race(name, raceDateTime, carCategory, circuit, season);
    }


    public LocalDateTime getLobbyAnnouncementTime() {
        return this.raceDateTime.minusMinutes(this.lobbyLeadTimeMinutes);
    }

    public void updateName(String newName) {
        String name = CommonValidation.requiredText(newName, "Race", 5);
        updateField(name, this.name, value -> {
            this.name = value;
        });
    }

    public void updateRaceDateTime(LocalDateTime newRaceDateTime) {
        CommonValidation.required(newRaceDateTime, "Race date");
        validateRaceDate(newRaceDateTime, this.season);
        updateField(newRaceDateTime, this.raceDateTime, value -> {
            this.raceDateTime = value;
        });
    }

    public void updateLobbyLeadTime(Integer newLeadTime) {
        if (newLeadTime == null || newLeadTime < 0) {
            throw new BusinessRuleException("Lobby lead time cannot be negative.");
        }
        updateField(newLeadTime, this.lobbyLeadTimeMinutes, value -> {
            this.lobbyLeadTimeMinutes = value;
        });
    }

    public void updateCarCategory(CarCategory newCarCategory) {
        CommonValidation.required(newCarCategory, "Car category");
        updateField(newCarCategory, this.carCategory, value -> {
            this.carCategory = value;
        });
    }

    public void updateCircuit(String newCircuit) {
        CommonValidation.requiredText(newCircuit, "Circuit", 5);
        updateField(newCircuit, this.circuit, value -> {
            this.circuit = value;
        });
    }

    private void validateRaceDate(LocalDateTime dateTime, Season season) {
        CommonValidation.required(dateTime, "Race date");
        CommonValidation.required(season, "Season");
        LocalDate raceDate = dateTime.toLocalDate();
        if (raceDate.isBefore(season.getStartDate()) || raceDate.isAfter(season.getEndDate())) {
            throw new BusinessRuleException("Race date is outside the season interval.");
        }
    }

}
