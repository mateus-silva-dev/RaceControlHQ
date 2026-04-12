package com.racecontrol.api.model;

import com.racecontrol.api.support.assertions.DomainAssertions;
import com.racecontrol.api.domain.model.Season;
import com.racecontrol.api.domain.model.enums.SeasonStatus;
import com.racecontrol.api.support.builders.SeasonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Season")
public class SeasonTest implements DomainAssertions {

    private Season season;

    private static final LocalDate TODAY = LocalDate.of(2026, Month.JANUARY, 1);
    private static final Clock FIXED_CLOCK = Clock.fixed(
            TODAY.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                    ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        season = SeasonBuilder.season().build();
    }

    @Nested
    @DisplayName("Creation Scenarios")
    class SeasonCreation {

        @Test
        @DisplayName("Should successfully create a season with default builder values")
        void shouldCreateSeasonSuccessfully() {
            assertAll(
                    () -> assertEquals("Season 2026", season.getTitle()),
                    () -> assertEquals(LocalDate.of(2026, Month.MAY, 1), season.getStartDate()),
                    () -> assertEquals(LocalDate.of(2026, Month.MAY, 31), season.getEndDate()),
                    () -> assertEquals(SeasonStatus.REGISTRATION_OPEN, season.getStatus()),
                    () -> assertEquals("GT World Challenger 2026", season.getLeague().getName())
            );
        }

        @ParameterizedTest
        @CsvSource(value = {
            "NULL, 'Season title cannot be empty.'",
            "'', 'Season title cannot be empty.'",
            "S2, 'Season title must be at least 3 characters long.'"
        }, nullValues = {"NULL"})
        @DisplayName("Should throw exception for invalid title during creation")
        void shouldThrowWhenCreationTitleAreInvalid(String title, String message) {
            assertThatBusinessException(
                    () -> SeasonBuilder.season().withTitle(title).build(),
                    message
            );
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NULL, 2026-05-31, 'Dates cannot be null.'",
                "2026-05-01, NULL, 'Dates cannot be null.'",
                "2026-01-01, 2026-05-31, 'The start date cannot be in the past.'",
                "2026-05-01, 2026-01-01, 'The end date cannot be before start date.'",
                "2026-05-01, 2026-05-01, 'The season must last at least one day.'"
        }, nullValues = {"NULL"})
        @DisplayName("Should validate season date chronology")
        void shouldThrowExceptionForInvalidDates(LocalDate start, LocalDate end, String message) {
            assertThatBusinessException(
                    () -> SeasonBuilder.season()
                            .withStartDate(start)
                            .withEndDate(end)
                            .build(),
                    message
            );
        }

        @Test
        @DisplayName("Should throw exception for invalid league during creation")
        void shouldThrowWhenCreationWithInvalidLeague() {
            assertThatBusinessException(
                    () -> SeasonBuilder.season().withLeague(null).build(),
                    "League is mandatory."
            );
        }
    }

    @Nested
    @DisplayName("Updates Scenarios")
    class SeasonUpdate {

        @Nested
        @DisplayName("Title")
        class SeasonTitleUpdate {

            @Test
            @DisplayName("Should update title")
            void shouldUpdateTitle() {
                assertUpdateWorkflow(
                        season::updateTitle,
                        season::getTitle,
                        "   Season   2026   -    Portuguese   League",
                        "Season 2026 - Portuguese League"
                );
            }

            @ParameterizedTest
            @CsvSource(value = {
                    "'', 'Season title cannot be empty.'",
                    "'S2', 'Season title must be at least 3 characters long.'"
            })
            @DisplayName("Should validate title")
            void shouldValidateTitle(String title, String message) {
                assertThatBusinessException(() -> season.updateTitle(title), message);
            }
        }

        @Nested
        @DisplayName("Date")
        class SeasonDateUpdate {

            @Test
            @DisplayName("Should update start date and end date")
            void shouldUpdateStartAndEndDates() {
                LocalDate start = TODAY.plusMonths(5);
                LocalDate end = TODAY.plusMonths(7);

                season.updateDates(start, end, FIXED_CLOCK);

                assertAll(
                        () -> assertEquals(start, season.getStartDate()),
                        () -> assertEquals(end, season.getEndDate())
                );
            }

            @ParameterizedTest
            @CsvSource(value = {
                    "NULL, 2026-05-31, 'Dates cannot be null.'",
                    "2026-05-01, NULL, 'Dates cannot be null.'",
                    "2026-05-01, 2026-01-01, 'The end date cannot be before start date.'",
                    "2026-05-01, 2026-05-01, 'The season must last at least one day.'"
            }, nullValues = {"NULL"})
            @DisplayName("Should validate dates")
            void shouldValidateDate(LocalDate start, LocalDate end, String message) {
                assertThatBusinessException(() -> season.updateDates(start, end, FIXED_CLOCK), message);
            }

            @Test
            @DisplayName("Should update when only start date changes")
            void shouldUpdateWhenOnlyStartDateChanges() {
                LocalDate start = TODAY.plusMonths(5);
                LocalDate end = TODAY.plusMonths(7);

                season.updateDates(start, end, FIXED_CLOCK);

                LocalDate newStart = start.plusDays(1);

                season.updateDates(newStart, end, FIXED_CLOCK);

                assertAll(
                        () -> assertEquals(newStart, season.getStartDate()),
                        () -> assertEquals(end, season.getEndDate())
                );
            }

            @Test
            @DisplayName("Should update when only end date changes")
            void shouldUpdateWhenOnlyEndDateChanges() {
                LocalDate start = TODAY.plusMonths(5);
                LocalDate end = TODAY.plusMonths(7);

                season.updateDates(start, end, FIXED_CLOCK);

                LocalDate newEnd = end.plusDays(1);

                season.updateDates(start, newEnd, FIXED_CLOCK);

                assertAll(
                        () -> assertEquals(start, season.getStartDate()),
                        () -> assertEquals(newEnd, season.getEndDate())
                );
            }

            @Test
            @DisplayName("Should do nothing when dates are unchanged")
            void shouldDoNothingWhenDatesAreUnchanged() {
                LocalDate start = TODAY.plusMonths(5);
                LocalDate end = TODAY.plusMonths(7);

                season.updateDates(start, end, FIXED_CLOCK);
                season.updateDates(start, end, FIXED_CLOCK);

                assertAll(
                        () -> assertEquals(start, season.getStartDate()),
                        () -> assertEquals(end, season.getEndDate())
                );
            }

            @Test
            @DisplayName("Should throw exception when start date is before today")
            void shouldThrowWhenStartDateIsBeforeToday() {
                LocalDate yesterday = TODAY.minusDays(1);
                assertThatBusinessException(
                        () -> season.updateDates(yesterday, TODAY.plusMonths(1), FIXED_CLOCK), "The start date cannot be in the past."
                );
            }


        }
    }

    @Nested
    @DisplayName("Status Scenarios")
    class StatusTransitions {

        private static Stream<Arguments> validStatusScenarios() {
            return Stream.of(
                    Arguments.of(SeasonStatus.REGISTRATION_OPEN, SeasonStatus.IN_PROGRESS),
                    Arguments.of(SeasonStatus.IN_PROGRESS, SeasonStatus.FINISHED));
        }

        private static Stream<Arguments> invalidStatusScenarios() {
            return Stream.of(
                    Arguments.of(SeasonStatus.REGISTRATION_OPEN, SeasonStatus.FINISHED),
                    Arguments.of(SeasonStatus.FINISHED, SeasonStatus.IN_PROGRESS),
                    Arguments.of(SeasonStatus.IN_PROGRESS, SeasonStatus.REGISTRATION_OPEN));
        }

        @ParameterizedTest
        @MethodSource("validStatusScenarios")
        @DisplayName("Should update status correctly")
        void shouldTransitionBetweenValidStatuses(SeasonStatus initial, SeasonStatus next) {
            season.updateStatus(initial);
            assertUpdateWorkflow(
                    season::updateStatus,
                    season::getStatus,
                    next,
                    next
            );
        }

        @ParameterizedTest
        @MethodSource("invalidStatusScenarios")
        @DisplayName("Should block invalid status transitions")
        void shouldThrowOnInvalidTransition(SeasonStatus initial, SeasonStatus invalidNext) {
            if (initial == SeasonStatus.IN_PROGRESS) {
                season.updateStatus(SeasonStatus.IN_PROGRESS);
            } else if (initial == SeasonStatus.FINISHED) {
                season.updateStatus(SeasonStatus.IN_PROGRESS);
                season.updateStatus(SeasonStatus.FINISHED);
            }
            assertThatBusinessException(
                    () -> season.updateStatus(invalidNext),
                    "Cannot transition from " + initial + " to " + invalidNext
            );
        }

    }


}
