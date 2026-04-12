package com.racecontrol.api.modelTest;

import com.racecontrol.api.builders.DriverBuilder;
import com.racecontrol.api.builders.RaceBuilder;
import com.racecontrol.api.domain.model.Driver;
import com.racecontrol.api.domain.model.enums.CarCategory;
import com.racecontrol.api.domain.model.valueObject.Nationality;
import com.racecontrol.api.helpers.DomainAssertions;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Driver")
public class DriverTest implements DomainAssertions {

    private Clock fixedClock;
    private LocalDate today;

    private Driver driver;

    @BeforeEach
    void setUp() {
        today = LocalDate.of(2026, Month.APRIL, 11);
        fixedClock = Clock.fixed(today.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        driver = DriverBuilder.driver().build();
    }

    @Nested
    @DisplayName("Creation Scenarios")
    class DriverCreation {

        @Test
        @DisplayName("Should successfully create a driver with default builder values")
        void shouldCreateDriveSuccessfully() {
            assertAll(
                    () -> assertEquals("Lewis Hamilton", driver.getName()),
                    () -> assertEquals(LocalDate.of(1999, Month.JUNE, 10), driver.getBirthDate()),
                    () -> assertEquals(new Nationality("GB"), driver.getNationality()),
                    () -> assertEquals("xX_LewisHamilton_Xx", driver.getGamerTag()),
                    () -> assertTrue(driver.isActive())
            );
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NULL, 1999-06-10, 'GB', 'xX_LewisHamilton_Xx', 'Name cannot be empty.'",
                "'', 1999-06-10, 'GB', 'xX_LewisHamilton_Xx', 'Name cannot be empty.'",
                "'   ', 1999-06-10, 'GB', 'xX_LewisHamilton_Xx', 'Name cannot be empty.'",
                "'Lw', 1999-06-10, 'GB', 'xX_LewisHamilton_Xx', 'Name must be at least 3 characters long.'",

                "'Lewis Hamilton', NULL, 'GB', 'xX_LewisHamilton_Xx', 'Birth date is mandatory.'",
                "'Lewis Hamilton', 2027-06-10, 'GB', 'xX_LewisHamilton_Xx', 'Birth date cannot be in the future.'",

                "'Lewis Hamilton', 1999-06-10, NULL, 'xX_LewisHamilton_Xx', 'Nationality is mandatory.'",
                "'Lewis Hamilton', 1999-06-10, 'WZ', 'xX_LewisHamilton_Xx', 'Invalid country code.'",
                "'Lewis Hamilton', 1999-06-10, 'TYX', 'xX_LewisHamilton_Xx', 'Invalid country code.'",

                "'Lewis Hamilton', 1999-06-10, 'GB', NULL, 'Gamer tag cannot be empty.'",
                "'Lewis Hamilton', 1999-06-10, 'GB', '', 'Gamer tag cannot be empty.'",
                "'Lewis Hamilton', 1999-06-10, 'GB', '   ', 'Gamer tag cannot be empty.'",
                "'Lewis Hamilton', 1999-06-10, 'GB', 'LW', 'Gamer tag must be at least 3 characters long.'",
        }, nullValues = {"NULL"})
        @DisplayName("Should throw exception for invalid inputs during creation")
        void shouldThrowWhenCreationInputsAreInvalid(String name, LocalDate birthDate, String countryCode, String gamerTag, String message) {
            assertThatBusinessException(() -> {
                Nationality nationality = (countryCode == null) ? null : new Nationality(countryCode);
                Driver.create(name, birthDate, nationality, gamerTag, fixedClock);
            }, message);
        }

    }

    @Nested
    @DisplayName("Update Scenarios")
    class DriverUpdate {

        @Nested
        @DisplayName("Name")
        class DriverNameUpdate {

            @Test
            @DisplayName("Should update name")
            void shouldUpdateName() {
                assertUpdateWorkflow(
                        driver::updateName,
                        driver::getName,
                        "   Max   Verstappen  ",
                        "Max Verstappen"
                );
            }
        }

        @Nested
        @DisplayName("Gamer tag")
        class DriverGamerTagUpdate {

            @Test
            @DisplayName("Should update gamer tag")
            void shouldUpdateGamerTag() {
                assertUpdateWorkflow(
                        driver::updateGamerTag,
                        driver::getGamerTag,
                        "   MaxVerstappen33  ",
                        "MaxVerstappen33"
                );
            }
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NAME, '', 'Name cannot be empty.'",
                "NAME, '   ', 'Name cannot be empty.'",
                "NAME, 'LW', 'Name must be at least 3 characters long.'",

                "GAMERTAG, '', 'Gamer tag cannot be empty.'",
                "GAMERTAG, '   ', 'Gamer tag cannot be empty.'",
                "GAMERTAG, 'LW', 'Gamer tag must be at least 3 characters long.'",
        })
        @DisplayName("Should validate all fields on update")
        void shouldValidateFieldsOnUpdate(String field, String value, String message) {
            assertThatBusinessException(() -> {
                switch (field) {
                    case "NAME" -> driver.updateName(value);
                    case "GAMERTAG" -> driver.updateGamerTag(value);
                    default -> throw new IllegalArgumentException("Unknown field: " + field);
                }
            }, message);
        }
    }

    @Test
    @DisplayName("Should calculate age correctly")
    void shouldCalculateAge() {
        assertEquals(26, driver.getAge(fixedClock));
    }
}
