package com.racecontrol.api.domain.validation;

import com.racecontrol.api.support.assertions.DomainAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("URL Validation")
public class UrlValidationTest implements DomainAssertions {

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldThrowWhenEmpty(String input) {
        assertThatBusinessException(
                () -> UrlValidation.validateUrl(input, "LogoURL"),
                "LogoURL cannot be empty."
        );
    }

    @Test
    void shouldThrowWhenNull() {
        assertThatBusinessException(
                () -> UrlValidation.validateUrl(null, "LogoURL"),
                "LogoURL cannot be empty."
        );
    }

    @ParameterizedTest
    @CsvSource({
            "http://google.com, https://google.com",
            "https://google.com, https://google.com",
            "google.com, https://google.com",
            "WWW.RACE.COM, https://www.race.com"
    })
    void shouldForceHttpsAndNormalize(String input, String expected) {
        assertEquals(expected, UrlValidation.validateUrl(input, "Field"));
    }

    @ParameterizedTest
    @ValueSource(strings = {":::::", "espaço no meio.com", "http://^invalid^.com"})
    void shouldThrowForClearlyInvalidUrls(String input) {
        assertThatBusinessException(
                () -> UrlValidation.validateUrl(input, "Logo"),
                "The URL for Logo is invalid."
        );
    }

    @Test
    void shouldThrowWhenFinalUrlIsInvalid() {
        assertThatBusinessException(
                () -> UrlValidation.validateUrl("invalid-url", "Logo"),
                "The URL for Logo is invalid."
        );
    }

    @ParameterizedTest
    @CsvSource({
            "HTTP://RaceControl.Com/Logo.PNG, https://racecontrol.com/Logo.PNG",
            "https://RACE.com, https://race.com",
            "WWW.GOOGLE.COM, https://www.google.com"
    })
    void shouldHandleCaseSensitivity(String input, String expected) {
        String result = UrlValidation.validateUrl(input, "Field");
        assertEquals(expected.toLowerCase(), result.toLowerCase());
    }

}
