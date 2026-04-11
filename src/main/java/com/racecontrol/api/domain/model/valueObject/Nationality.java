package com.racecontrol.api.domain.model.valueObject;

import com.racecontrol.api.core.exception.BusinessRuleException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.Set;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "countryCode")
public class Nationality {

    private static final Set<String> ISO_CODES = Set.of(Locale.getISOCountries());

    @Column(name = "nationality", nullable = false, length = 2)
    private String countryCode;

    public Nationality(String countryCode) {
        String normalized = countryCode == null ? null : countryCode.trim().toUpperCase();

        if (normalized == null || !ISO_CODES.contains(normalized)) {
            throw new BusinessRuleException("Invalid country code");
        }

        this.countryCode = normalized;
    }
}
