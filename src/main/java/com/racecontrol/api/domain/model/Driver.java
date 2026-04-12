package com.racecontrol.api.domain.model;

import com.racecontrol.api.core.exception.BusinessRuleException;
import com.racecontrol.api.domain.model.valueObject.Nationality;
import com.racecontrol.api.domain.validation.CommonValidation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Entity
@Table(name = "driver")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "id")
public class Driver extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Embedded
    @Column(nullable = false)
    private Nationality nationality;

    @Column(nullable = false, unique = true, length = 100)
    private String gamerTag;

    @Column(nullable = false)
    private boolean active;

    public Driver(String name, LocalDate birthDate, Nationality nationality, String gamerTag, Clock clock) {
        this.name = CommonValidation.requiredText(name, "Name", 3);
        this.birthDate = CommonValidation.required(birthDate, "Birth date");
        validateBirthDate(clock);
        this.nationality = CommonValidation.required(nationality, "Nationality");
        this.gamerTag = CommonValidation.requiredText(gamerTag, "Gamer tag", 3);
        this.active = true;
    }

    public static Driver create(String name, LocalDate birthDate, Nationality nationality, String gamerTag, Clock clock) {
        return new Driver(name, birthDate, nationality, gamerTag, clock);
    }


    public void updateName(String newName) {
        String name = CommonValidation.requiredText(newName, "Name", 3);
        updateField(name, this.name, value -> {
            this.name = value;
        });
    }

    public void updateGamerTag(String newGamerTag) {
        String gamerTag = CommonValidation.requiredText(newGamerTag, "Gamer tag", 3);
        updateField(gamerTag, this.gamerTag, value -> {
            this.gamerTag = value;
        });
    }

    public Integer getAge(Clock clock) {
        return Period.between(this.birthDate, LocalDate.now(clock)).getYears();
    }


    private void validateBirthDate(Clock clock) {
        if (birthDate.isAfter(LocalDate.now(clock))) {
            throw new BusinessRuleException("Birth date cannot be in the future.");
        }
    }

}
