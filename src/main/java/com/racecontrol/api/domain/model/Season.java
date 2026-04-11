package com.racecontrol.api.domain.model;

import com.racecontrol.api.core.exception.BusinessRuleException;
import com.racecontrol.api.domain.model.enums.SeasonStatus;
import com.racecontrol.api.domain.validation.CommonValidation;
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
@EqualsAndHashCode(callSuper = false, of = "id")
public class Season extends BaseEntity {

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
        CommonValidation.required(league, "League");

        this.title = CommonValidation.requiredText(title, "Season title", 3);
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
        String title = CommonValidation.requiredText(newTitle, "Season title", 3);
        updateField(title, this.title, value -> {
            this.title = value;
        });
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
        updateField(newStatus, this.status, value -> {
            this.status.validateTransition(value);
            this.status = value;
        });
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

}
