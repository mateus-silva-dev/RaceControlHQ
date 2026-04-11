package com.racecontrol.api.domain.validation;

public class TextValidation {

    public static String validate(String value, String field, int minLength) {
        CommonValidation.validateNotBlank(value, field);
        String normalized = normalize(value);
        CommonValidation.validateMinLength(normalized, field, minLength);
        return normalized;
    }

    private static String normalize(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }





}
