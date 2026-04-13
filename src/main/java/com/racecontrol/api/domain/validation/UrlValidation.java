package com.racecontrol.api.domain.validation;

import com.racecontrol.api.core.code.Code;
import com.racecontrol.api.core.exception.BusinessRuleException;

public class UrlValidation {

    public static String validateUrl(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException(field + " cannot be empty.", Code.EMPTY_FIELD);
        }

        String url = value.trim();

         if (isClearlyInvalid(url)) {
            throw new BusinessRuleException(
                    "The URL for " + field + " is invalid.",
                    Code.INVALID_URL
            );
        }

        String lower = url.toLowerCase();
        if (!lower.startsWith("http")) {
            url = "https://" + url.toLowerCase();
        } else {
            int protocolEnd = url.indexOf("://") + 3;
            int firstSlash = url.indexOf("/", protocolEnd);

            if (firstSlash == -1) {
                url = url.toLowerCase();
            } else {
                String protocolAndDomain = url.substring(0, firstSlash).toLowerCase();
                String path = url.substring(firstSlash);
                url = protocolAndDomain + path;
            }
        }

        if (url.startsWith("http://")) {
            url = "https://" + url.substring(7);
        }

        if (!isValidUrl(url)) {
            throw new BusinessRuleException(
                    "The URL for " + field + " is invalid.",
                    Code.INVALID_URL
            );
        }

        return url;
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
