package com.racecontrol.api.domain.model;

import com.racecontrol.api.support.assertions.DomainAssertions;
import com.racecontrol.api.domain.model.enums.CarCategory;
import com.racecontrol.api.support.builders.RaceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Race")
public class RaceTest implements DomainAssertions {

    private Race race;

    @BeforeEach
    void setUp() {
        race = RaceBuilder.race().build();
    }

    @Test
    @DisplayName("Should create race and validate invalid inputs")
    void shouldCreate() {
        assertEquals("Nürburgring Grand-Prix", race.getName());
        assertAll(
                () -> assertThatBusinessException(
                        () -> RaceBuilder.race().withCategory(null).build(),
                        "Car category is mandatory."
                ),
                () -> assertThatBusinessException(
                        () -> RaceBuilder.race().withRaceTime(null).build(),
                        "Race date is mandatory.")
        );
    }

    @Nested
    @DisplayName("Updates Name workflow")
    class UpdateName {
        @Test
        void shouldUpdateName() {
            assertUpdateWorkflow(
                    race::updateName, race::getName,
                    "   Interlagos  Heineken    Grand-Prix         GT3", "Interlagos Heineken Grand-Prix GT3"
            );
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> race.updateName("Nürburgring Grand-Prix"), race::getName);
        }
    }

    @Nested
    @DisplayName("Update Race DateTime workflow")
    class UpdateRaceDateTime {
        @Test
        void shouldUpdateRaceDateTime() {
            assertUpdateWorkflow(
                    race::updateRaceDateTime, race::getRaceDateTime,
                    LocalDateTime.of(2026, Month.MAY, 25, 20, 0),
                    LocalDateTime.of(2026, Month.MAY, 25, 20, 0)
            );
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> race.updateRaceDateTime(LocalDateTime.of(2026, Month.MAY, 10, 20, 0)), race::getRaceDateTime);
        }

        @ParameterizedTest
        @ValueSource(strings = {"2026-01-10T20:00:00", "2026-10-20T20:00:00"})
        @DisplayName("Should guard season interval")
        void shouldThrowWhenDateIsOutsideSeason(String dateTimeStr) {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
            assertThatBusinessException(
                    () -> RaceBuilder.race().withRaceTime(dateTime).build(),
                    "Race date is outside the season interval."
            );
        }
    }

    @Nested
    @DisplayName("Update Lobby Lead Time workflow")
    class LobbyLeadTimeUpdate {
        @Test
        void shouldUpdateLobbyLeadTime() {
            assertUpdateWorkflow(
                    race::updateLobbyLeadTime, race::getLobbyLeadTimeMinutes, 10, 10
            );
        }

        @Test
        void shouldCalculateLobbyTime() {
            LocalDateTime raceTime = LocalDateTime.of(2026, 5, 10, 20, 0);
            Race race = RaceBuilder.race().withRaceTime(raceTime).build();
            LocalDateTime expected = LocalDateTime.of(2026, 5, 10, 19, 45);
            assertEquals(expected, race.getLobbyAnnouncementTime());
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> race.updateLobbyLeadTime(15), race::getLobbyLeadTimeMinutes);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"-10", "-30"})
        void shouldThrowWhenMinutesNegative(String value) {
            Integer invalidMinutes = (value != null) ? Integer.valueOf(value) : null;
            assertThatBusinessException(
                    () -> race.updateLobbyLeadTime(invalidMinutes),
                    "Lobby lead time cannot be negative."
            );
        }
    }

    @Nested
    @DisplayName("Update Car Category workflow")
    class UpdateCarCategory {
        @Test
        void shouldUpdateCarCategory() {
            assertUpdateWorkflow(race::updateCarCategory, race::getCarCategory, CarCategory.GR4, CarCategory.GR4);
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> race.updateCarCategory(CarCategory.GR3), race::getCarCategory);
        }
    }

    @Nested
    @DisplayName("Update Circuit workflow")
    class UpdateCircuit {
        @Test
        void shouldUpdateCircuit() {
            assertUpdateWorkflow(race::updateCircuit, race::getCircuit, "Suzuka Circuit", "Suzuka Circuit");
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> race.updateCircuit("Nürburgring"), race::getCircuit);
        }
    }
}