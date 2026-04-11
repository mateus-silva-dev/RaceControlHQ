package com.racecontrol.api.domain.model;

import com.racecontrol.api.core.exception.BusinessRuleException;
import com.racecontrol.api.domain.model.enums.SeasonStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "season")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private SeasonStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private League league;

    public Season(String title, LocalDate startDate, LocalDate endDate, League league, Clock clock) {
        if (league == null) {
            throw new BusinessRuleException("League is mandatory for a season.");
        }

        this.title = validateTitle(title);
        validateDates(startDate, endDate, clock);
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = SeasonStatus.REGISTRATION_OPEN;
        this.league = league;
    }

    public static Season create(String title, LocalDate startDate, LocalDate endDate, League league, Clock clock) {
        return new Season(title, startDate, endDate, league, clock);
    }


    public void updateTitle(String newTitle) {
        String title = validateTitle(newTitle);
        if (isUnchanged(title, this.title)) return;
        this.title = title;
    }

    public void updateDates(LocalDate newStartDate, LocalDate newEndDate, Clock clock) {
        validateDates(newStartDate, newEndDate, clock);
        if (isUnchanged(newStartDate, this.startDate) && isUnchanged(newEndDate, this.endDate)) {
            return;
        }
        this.startDate = newStartDate;
        this.endDate = newEndDate;
    }

    public void updateStatus(SeasonStatus newStatus) {
        if (isUnchanged(newStatus, this.status)) return;
        this.status.validateTransition(newStatus);
        this.status = newStatus;
    }


    private String validateTitle(String title) {
        if (title == null) {
            throw new BusinessRuleException("Season title cannot be null.");
        }
        String value = title.trim().replaceAll("\\s+", " ");
        if (value.isEmpty()) {
            throw new BusinessRuleException("Season title cannot be empty.");
        }
        if (value.trim().length() < 3) {
            throw new BusinessRuleException("Season title must be at least 3 characters long.");
        }
        return value;
    }

    private void validateDates(LocalDate start, LocalDate end, Clock clock) {
        if (start == null || end == null) {
            throw new BusinessRuleException("Dates cannot be null.");
        }

        LocalDate today = LocalDate.now(clock);

        if(start.isBefore(today)) {
            throw new BusinessRuleException("The start date cannot be in the past.");
        }
        if (end.isBefore(start)) {
            throw new BusinessRuleException("The end date cannot be before start date.");
        }
        if (end.isEqual(start)) {
            throw new BusinessRuleException("The season must last at least one day.");
        }
    }

    private <T> boolean isUnchanged(T newValue, T currentValue) {
        return Objects.equals(newValue, currentValue);
    }

}
