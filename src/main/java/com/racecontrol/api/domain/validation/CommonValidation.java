package com.racecontrol.api.domain.validation;

import com.racecontrol.api.core.code.Code;
import com.racecontrol.api.core.exception.BusinessRuleException;

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

}
