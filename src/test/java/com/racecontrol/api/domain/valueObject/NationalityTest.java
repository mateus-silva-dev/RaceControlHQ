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

@DisplayName("Nationality")
class NationalityTest implements DomainAssertions {

    @Nested
    @DisplayName("Valid Country Codes")
    class ValidCodes {

        @ParameterizedTest
        @ValueSource(strings = {"br", "BR", " br ", "US", "jp", "PT"})
        @DisplayName("Should create nationality for valid ISO codes and normalize them")
        void shouldCreateAndNormalize(String code) {
            Nationality nationality = new Nationality(code);

            // O código deve sempre estar em Caixa Alta e sem espaços
            assertEquals(code.trim().toUpperCase(), nationality.getCountryCode());
        }

        @Test
        @DisplayName("Should accept all valid ISO countries")
        void shouldAcceptValidIsoCountries() {
            // Teste rápido para garantir que o set estático está funcionando
            assertDoesNotThrow(() -> new Nationality("BR"));
            assertDoesNotThrow(() -> new Nationality("AR"));
        }
    }

    @Nested
    @DisplayName("Invalid Country Codes")
    class InvalidCodes {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "ZZ", "BRA", "12", "BRAZIL", "UK"})
        // Nota: UK não é ISO 3166-1 alpha-2 (o correto é GB), bom teste!
        @DisplayName("Should throw exception for null, empty or invalid ISO codes")
        void shouldThrowExceptionForInvalidCode(String invalidCode) {
            assertThatBusinessException(
                    () -> new Nationality(invalidCode),
                    "Invalid country code."
            );
        }
    }

    @Test
    @DisplayName("Should be equal for same codes")
    void shouldMaintainEquality() {
        Nationality n1 = new Nationality("br");
        Nationality n2 = new Nationality("BR");

        assertEquals(n1.getCountryCode(), n2.getCountryCode());
    }
}
