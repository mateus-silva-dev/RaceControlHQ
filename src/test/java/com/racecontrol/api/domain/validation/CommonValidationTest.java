package com.racecontrol.api.domain.validation;

import com.racecontrol.api.support.assertions.DomainAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Common Validation")
public class CommonValidationTest implements DomainAssertions {

    @Nested
    @DisplayName("Required Object Validation")
    class requiredObject {

        @Test
        @DisplayName("Should return value when it is not null")
        void shouldReturnValue() {
            String input = "Valid";
            String result = CommonValidation.required(input, "Field");
            assertEquals(input, result);
        }

        @Test
        @DisplayName("Should throw BusinessRuleException when null")
        void shouldThrowException() {
            assertThatBusinessException(
                    () -> CommonValidation.required(null, "Nationality"),
                    "Nationality is mandatory.");
        }

    }

    @Nested
    @DisplayName("Required Text Validation")
    class requiredText {

        @Test
        @DisplayName("Should return value normalized when it is valid")
        void shouldNormalizeSpacesAndTrim() {
            String input = "   Lewis   Hamilton   ";
            String expected = "Lewis Hamilton";
            assertEquals(expected, CommonValidation.requiredText(input, "Name", 3));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NULL, 'Name', 'Name cannot be empty.'",
                "'', 'Name', 'Name cannot be empty.'",
                "'   ', 'Name', 'Name cannot be empty.'",
                "'Ab', 'Name', 'Name must be at least 3 characters long.'"
        }, nullValues = {"NULL"})
        @DisplayName("Should throw BusinessRuleException when invalid input")
        void shouldThrowException(String input, String field, String message) {
            assertThatBusinessException(
                    () -> CommonValidation.requiredText(input, field, 3), message);
        }

    }

    @Nested
    @DisplayName("Required Minimum Age")
    class requiredMinimumAge {
        @Test
        void shouldValidateBirthDate() {
            LocalDate birthDate = LocalDate.of(1990, Month.JUNE, 10);
            assertEquals(birthDate, CommonValidation.requiredMinAge(birthDate, "Birth date", 13));
        }

        @Test
        void shouldFailWhenDriverIsExactlyOneDayTooYoung() {
            LocalDate tooYoung = LocalDate.now().minusYears(13).plusDays(1);
            assertThatBusinessException(
                    () -> CommonValidation.requiredMinAge(tooYoung, "Birth date", 13),
                    "Birth date must be at least 13 years old."
            );
        }
    }

}
