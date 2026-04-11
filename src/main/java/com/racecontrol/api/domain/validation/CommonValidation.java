package com.racecontrol.api.domain.validation;

import com.racecontrol.api.core.code.Code;
import com.racecontrol.api.core.exception.BusinessRuleException;

public class CommonValidation {

    public static void validateNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException(field + " cannot be empty.", Code.EMPTY_FIELD);
        }
    }

    public static void validateMinLength(String value, String field, int minLength) {
        if (value.length() < minLength) {
            throw new BusinessRuleException(
                    field + " must be at least " + minLength + " characters long."
            );
        }
    }

}
