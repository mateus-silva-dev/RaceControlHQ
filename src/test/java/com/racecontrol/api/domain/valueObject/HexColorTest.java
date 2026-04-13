package com.racecontrol.api.domain.valueObject;

import com.racecontrol.api.support.assertions.DomainAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("HexColor")
public class HexColorTest implements DomainAssertions {

    @Nested
    @DisplayName("Fallback & Normalization")
    class FallbackAndNormalization {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        @DisplayName("Should fallback to black when value is null, empty or blank")
        void shouldFallbackToBlack(String value) {
            HexColor hex = new HexColor(value);
            assertEquals("#000000", hex.getValue());
        }

        @ParameterizedTest
        @ValueSource(strings = {"ffffff", "#ffffff", "FfFfFf"})
        @DisplayName("Should normalize hex values to uppercase with hash")
        void shouldNormalize(String value) {
            HexColor hex = new HexColor(value);
            assertEquals("#FFFFFF", hex.getValue());
        }

        @Test
        @DisplayName("Should support short hex format (3 digits)")
        void shouldSupportShortHex() {
            HexColor hex = new HexColor("abc");
            assertEquals("#ABC", hex.getValue());
        }
    }

    @Nested
    @DisplayName("Invalid Formats")
    class InvalidFormats {

        @ParameterizedTest
        @ValueSource(strings = {"G12345", "#12345", "#1234567", "!", "12"})
        @DisplayName("Should throw exception for malformed hex colors")
        void shouldThrowExceptionForInvalidHex(String invalidValue) {
            assertThatBusinessException(
                    () -> new HexColor(invalidValue),
                    "Invalid HEX color."
            );
        }
    }

    @Test
    @DisplayName("Should be equal when values are same")
    void shouldMaintainEquality() {
        HexColor color1 = new HexColor("#FFFFFF");
        HexColor color2 = new HexColor("ffffff");

        assertEquals(color1.getValue(), color2.getValue());
    }

    @Nested
    @DisplayName("Regex Validation Logic")
    class RegexValidation {

        @ParameterizedTest
        @ValueSource(strings = {"#FFFFFF", "#000000", "#1A2B3C", "#FFF", "#000", "#ABC", "#a1b2c3", "#ff0000"})
        @DisplayName("Should validate correct hex formats")
        void shouldValidateCorrectFormats(String candidate) {
            assertDoesNotThrow(() -> new HexColor(candidate));
        }

        @ParameterizedTest
        @ValueSource(strings = {"#12345", "#1234567", "#GGGGGG", "#12345!", "blue"})
        @DisplayName("Should reject incorrect hex formats")
        void shouldRejectIncorrectFormats(String candidate) {
            assertThatBusinessException(
                    () -> new HexColor(candidate),
                    "Invalid HEX color."
            );
        }
    }
}
