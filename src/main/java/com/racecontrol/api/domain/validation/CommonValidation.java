package com.racecontrol.api.domain.validation;

import com.racecontrol.api.core.code.Code;
import com.racecontrol.api.core.exception.BusinessRuleException;

import java.time.LocalDate;

public class CommonValidation {

    public static <T> T required(T value, String field) {
        if (value == null) {
            throw new BusinessRuleException(field + " is mandatory.", Code.EMPTY_FIELD);
        }
        return value;
    }

    public static String requiredText(String value, String field, int minLength) {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException(field + " cannot be empty.", Code.EMPTY_FIELD);
        }

        String normalized = value.trim().replaceAll("\\s+", " ");

        if (normalized.length() < minLength) {
            throw new BusinessRuleException(field + " must be at least " + minLength + " characters long.");
        }

        return normalized;
    }

    public static LocalDate requiredMinAge(LocalDate birthDate, String field, int minAge) {
        required(birthDate, field);

        LocalDate today = LocalDate.now();
        LocalDate minBirthDate = today.minusYears(minAge);

        if (birthDate.isAfter(minBirthDate)) {
            throw new BusinessRuleException(
                    field + " must be at least " + minAge + " years old.",
                    Code.INVALID_AGE
            );
        }

        return birthDate;
    }
}
