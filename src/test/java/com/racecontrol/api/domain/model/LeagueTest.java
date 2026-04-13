package com.racecontrol.api.domain.model;

import com.racecontrol.api.support.assertions.DomainAssertions;
import com.racecontrol.api.domain.model.League;
import com.racecontrol.api.support.builders.LeagueBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("League")
class LeagueTest implements DomainAssertions {

    private League league;

    @BeforeEach
    void setUp() {
        league = LeagueBuilder.league().build();
    }

    @Test
    @DisplayName("Should create team and validate invalid inputs")
    void shouldCreate() {
        assertEquals("GT World Challenger 2026", league.getName());
        assertAll(
                () -> assertThatBusinessException(
                        () -> LeagueBuilder.league().withName(null).build(),
                        "Name cannot be empty."
                ),
                () -> assertThatBusinessException(
                        () -> LeagueBuilder.league().withDescription("").build(),
                        "Description cannot be empty."
                )
        );
    }

    @Nested
    @DisplayName("Update Name workflow")
    class UpdateName {
        @Test
        void shouldUpdateName() {
            assertUpdateWorkflow(
                    league::updateName, league::getName,
                    "   Portuguese    GT3    League      ", "Portuguese GT3 League");
            assertEquals("portuguese-gt3-league", league.getSlug());
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> league.updateName("GT World Challenger 2026"), league::getName);
        }
    }

    @Nested
    @DisplayName("Update Description workflow")
    class UpdateDescription {
        @Test
        void shouldUpdateDescription() {
            assertUpdateWorkflow(
                    league::updateDescription, league::getDescription,
                    "   New    Regulation    2026   ", "New Regulation 2026");
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> league.updateDescription("New regulations. New stories."), league::getDescription);
        }
    }

    @Nested
    @DisplayName("Update URLs workflow")
    class UpdateUrls {
        @Test
        void shouldUpdateUrls() {
            assertUpdateWorkflow(league::updateLogoUrl, league::getLogoUrl, "google.com/logo.png", "https://google.com/logo.png");
            assertUpdateWorkflow(league::updateRulesPdfUrl, league::getRulesPdfUrl, "google.com/rules.pdf", "https://google.com/rules.pdf");
        }
    }

    @Nested
    @DisplayName("Configure Extra Points workflow")
    class ConfigureExtraPoints {
        @Test
        void shouldConfigureExtraPoints() {
            league.configureExtraPoints(1, 2);
            assertAll(
                    () -> assertEquals(1, league.getPointsPole()),
                    () -> assertEquals(2, league.getPointsFastestLap())
            );
        }

        @ParameterizedTest
        @CsvSource({
                "-1, 0, 'Points cannot be negative.'",
                "0, -1, 'Points cannot be negative.'",
                "-2, -2, 'Points cannot be negative.'"
        })
        void shouldThrowWhenExtraPointsAreNegative(int pole, int fastest, String message) {
            assertThatBusinessException(
                    () -> league.configureExtraPoints(pole, fastest), message);
        }
    }
}