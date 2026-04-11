package com.racecontrol.api.domain.validation;

import com.racecontrol.api.core.code.Code;
import com.racecontrol.api.core.exception.BusinessRuleException;

public class UrlValidation {

    public static String validateUrl(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException(field + " cannot be empty.", Code.EMPTY_FIELD);
        }

        String normalized = normalize(value);

        if (isClearlyInvalid(normalized)) {
            throw new BusinessRuleException(
                    "The URL for " + field + " is invalid.",
                    Code.INVALID_URL
            );
        }

        String lower = normalized.toLowerCase();

        if (lower.startsWith("http://")) {
            normalized = "https://" + normalized.substring(7);
        } else if (lower.startsWith("https://")) {
            normalized = "https://" + normalized.substring(8);
        } else {
            normalized = "https://" + normalized;
        }

        if (!isValidUrl(normalized)) {
            throw new BusinessRuleException(
                    "The URL for " + field + " is invalid.",
                    Code.INVALID_URL
            );
        }

        return normalized;
    }

    private static String normalize(String value) {
        return value.trim();
    }

    private static boolean isClearlyInvalid(String value) {
        return !value.contains(".") || value.contains(" ");
    }

    private static boolean isValidUrl(String url) {
        try {
            new java.net.URI(url);
            return url.contains(".");
        } catch (Exception e) {
            return false;
        }
    }
}
