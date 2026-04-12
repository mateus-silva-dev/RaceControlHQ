package com.racecontrol.api.modelTest;

import com.racecontrol.api.helpers.DomainAssertions;
import com.racecontrol.api.domain.model.Race;
import com.racecontrol.api.domain.model.enums.CarCategory;
import com.racecontrol.api.builders.RaceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Race")
public class RaceTest implements DomainAssertions {

    private Race race;

    @BeforeEach
    void setUp() {
        race = RaceBuilder.race().build();
    }

    @Nested
    @DisplayName("Creation Scenarios")
    class RaceCreation {

        @Test
        @DisplayName("Should successfully create a race with default builder values")
        void shouldCreateRaceSuccessfully() {
            assertAll(
                    () -> assertEquals("Nürburgring Grand-Prix", race.getName()),
                    () -> assertEquals(LocalDateTime.of(2026, Month.MAY, 10, 20, 0), race.getRaceDateTime()),
                    () -> assertEquals(CarCategory.GR3, race.getCarCategory()),
                    () -> assertEquals("Nürburgring", race.getCircuit()),
                    () -> assertEquals("GT World Challenger 2026", race.getSeason().getLeague().getName()),
                    () -> assertEquals("Season 2026", race.getSeason().getTitle())
            );
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NULL, GR3, 'Nürburgring', 'Race name cannot be empty.'",
                "'', GR3, 'Nürburgring', 'Race name cannot be empty.'",
                "'Nür', GR3, 'Nürburgring', 'Race name must be at least 5 characters long.'",

                "'Nürburgring Grand-Prix', NULL, 'Nürburgring', 'Car category is mandatory.'",

                "'Nürburgring Grand-Prix', GR3, NULL, 'Circuit cannot be empty.'",
                "'Nürburgring Grand-Prix', GR3, '', 'Circuit cannot be empty.'"
        }, nullValues = {"NULL"})
        @DisplayName("Should throw exception for invalid inputs during creation")
        void shouldThrowWhenCreationInputsAreInvalid(String name, CarCategory category, String circuit, String message) {
            assertThatBusinessException(
                    () -> RaceBuilder.race()
                            .withName(name)
                            .withCategory(category)
                            .withCircuit(circuit)
                            .build(),
                    message
            );
        }

        @Test
        @DisplayName("Should throw exception for null season during creation")
        void shouldThrowWhenCreationSeasonAreNull() {
            assertThatBusinessException(
                    () -> RaceBuilder.race()
                            .withSeason(null)
                            .build(),
                    "Season is mandatory."
            );
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NULL, 'Race date is mandatory.'",
                "2025-05-25T19:00:00, 'Race date is outside the season interval.'",
                "2027-09-05T19:00:00, 'Race date is outside the season interval.'"
        }, nullValues = {"NULL"})
        @DisplayName("Should throw exception for null or out-of-range race dates")
        void shouldThrowWhenTheDateIsIncorrectOrOutsideTheRange(LocalDateTime invalidDate, String message) {
            assertThatBusinessException(
                    () -> RaceBuilder.race()
                            .withRaceTime(invalidDate)
                            .build(),
                    message
            );
        }

    }

    @Nested
    @DisplayName("Updates Scenarios")
    class RaceUpdate {

        @Nested
        @DisplayName("Name")
        class RaceNameUpdate {

            @Test
            @DisplayName("Should update name")
            void shouldUpdateName() {
                assertUpdateWorkflow(
                        race::updateName,
                        race::getName,
                        "   Interlagos  Heineken    Grand-Prix         GT3",
                        "Interlagos Heineken Grand-Prix GT3"
                );
            }
        }

        @Nested
        @DisplayName("Race DateTime")
        class RaceDateTimeUpdate {

            @Test
            @DisplayName("Should update race date")
            void shouldUpdateRaceDate() {
                assertUpdateWorkflow(
                        race::updateRaceDateTime,
                        race::getRaceDateTime,
                        LocalDateTime.of(2026, Month.MAY, 25, 20, 0),
                        LocalDateTime.of(2026, Month.MAY, 25, 20, 0)
                );
            }
        }

        @Nested
        @DisplayName("Lobby Lead Time")
        class LobbyLeadTimeUpdate {

            @Test
            @DisplayName("Should calculate lobby time using default 30 minutes")
            void shouldCalculateLobbyTime() {
                LocalDateTime raceTime = LocalDateTime.of(2026, 5, 10, 20, 0);

                Race race = RaceBuilder.race()
                        .withRaceTime(raceTime)
                        .build();

                LocalDateTime expected = LocalDateTime.of(2026, 5, 10, 19, 45);

                assertEquals(expected, race.getLobbyAnnouncementTime());
            }

            @Test
            @DisplayName("Should update lobby lead time")
            void shouldUpdateLobbyLeadTime() {
                assertUpdateWorkflow(
                        race::updateLobbyLeadTime,
                        race::getLobbyLeadTimeMinutes,
                        10,
                        10
                );
            }
        }

        @Nested
        @DisplayName("Car Category")
        class CarCategoryUpdate {

            @Test
            @DisplayName("Should update car category")
            void shouldUpdateCarCategory() {
                assertUpdateWorkflow(
                        race::updateCarCategory,
                        race::getCarCategory,
                        CarCategory.GR4,
                        CarCategory.GR4
                );
            }
        }

        @Nested
        @DisplayName("Circuit")
        class CircuitUpdate {

            @Test
            @DisplayName("Should update circuit")
            void shouldUpdateCircuit() {
                assertUpdateWorkflow(
                        race::updateCircuit,
                        race::getCircuit,
                        "Suzuka Circuit",
                        "Suzuka Circuit"
                );
            }
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NAME, '', 'Race cannot be empty.'",
                "NAME, 'Itl', 'Race must be at least 5 characters long.'",
                "CIRCUIT, NULL, 'Circuit cannot be empty.'",
                "CIRCUIT, '', 'Circuit cannot be empty.'",
                "CATEGORY, NULL, 'Car category is mandatory.'",
                "LOBBYTIME, NULL, 'Lobby lead time cannot be negative.'",
                "LOBBYTIME, -10, 'Lobby lead time cannot be negative.'",
                "DATERACE, NULL, 'Race date is mandatory.'",
                "DATERACE, '2025-05-25T19:00:00', 'Race date is outside the season interval.'",
                "DATERACE, '2027-09-05T19:00:00', 'Race date is outside the season interval.'",
        }, nullValues = {"NULL"})
        @DisplayName("Should validate all fields on update")
        void shouldValidateFieldsOnUpdate(String field, String value, String message) {
            assertThatBusinessException(() -> {
                switch (field) {
                    case "NAME" -> race.updateName(value);
                    case "CIRCUIT" -> race.updateCircuit(value);
                    case "CATEGORY" -> race.updateCarCategory(
                            (value == null || value.isEmpty()) ? null : CarCategory.valueOf(value)
                    );
                    case "LOBBYTIME" -> race.updateLobbyLeadTime(
                            value == null ? null : Integer.parseInt(value)
                    );
                    case "DATERACE" -> race.updateRaceDateTime(
                            value == null ? null : LocalDateTime.parse(value)
                    );
                    default -> throw new IllegalArgumentException("Unknown field: " + field);
                }
            }, message);
        }


    }

}
