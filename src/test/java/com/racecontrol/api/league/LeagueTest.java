package com.racecontrol.api.league;

import com.racecontrol.api.helpers.DomainAssertions;
import com.racecontrol.api.domain.model.League;
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

    @Nested
    @DisplayName("Creation Scenarios")
    class LeagueCreation {

        @Test
        @DisplayName("Should successfully create a league with default builder values")
        void mustSuccessfullyCreateALeague() {
            assertAll(
                    () -> assertEquals("GT World Challenger 2026", league.getName()),
                    () -> assertEquals("New regulations. New stories.", league.getDescription()),
                    () -> assertEquals("gt-world-challenger-2026", league.getSlug()),
                    () -> assertNull(league.getLogoUrl()),
                    () -> assertNull(league.getRulesPdfUrl())
            );
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NULL, 'Valid description...', 'Name cannot be empty.'",
                "'', 'Valid description...', 'Name cannot be empty.'",
                "'GT7', 'Valid description...', 'Name must be at least 5 characters long.'",
                "'League Name', NULL, 'Description cannot be empty.'",
                "'League Name', '', 'Description cannot be empty.'",
                "'League Name', 'Short', 'Description must be at least 10 characters long.'"
        }, nullValues = {"NULL"})
        @DisplayName("Should throw exception for invalid name or description during creation")
        void shouldThrowWhenCreationInputsAreInvalid(String name, String description, String message) {
            assertThatBusinessException(
                    () -> LeagueBuilder.league().withName(name).withDescription(description).build(),
                    message
            );
        }
    }

    @Nested
    @DisplayName("Update Scenarios")
    class LeagueUpdate {

        @Test
        @DisplayName("Should update name and regenerate slugify")
        void shouldUpdateNameAndSlug() {
            assertUpdateWorkflow(
                    league::updateName,
                    league::getName,
                    "   Portuguese    GT3    League      ",
                    "Portuguese GT3 League"
            );
            assertEquals("portuguese-gt3-league", league.getSlug());
        }

        @Test
        @DisplayName("Should update description and normalize spaces")
        void shouldUpdateDescription() {
            assertUpdateWorkflow(
                    league::updateDescription,
                    league::getDescription,
                    "   New    Regulation    2026   ",
                    "New Regulation 2026"
            );
        }

        @Test
        @DisplayName("Should update logo and PDF URLs")
        void shouldUpdateUrls() {
            assertUpdateWorkflow(league::updateLogoUrl, league::getLogoUrl, "google.com/logo.png", "https://google.com/logo.png");
            assertUpdateWorkflow(league::updateRulesPdfUrl, league::getRulesPdfUrl, "google.com/rules.pdf", "https://google.com/rules.pdf");
        }

        @ParameterizedTest
        @CsvSource({
                "LOGO, 'google.com/logo.png', 'https://google.com/logo.png'",
                "LOGO, 'HTTP://RACE.COM/PIC.JPG', 'https://RACE.COM/PIC.JPG'",
                "PDF,  'race.com/rules.pdf', 'https://race.com/rules.pdf'",
                "PDF,  '  HTTPS://DOCS.COM/RULES  ', 'https://DOCS.COM/RULES'"
        })
        @DisplayName("Should normalize URLs by trimming and adding protocol when missing")
        void shouldNormalizeUrls(String field, String value, String expected) {
            switch (field) {
                case "LOGO" -> {
                    league.updateLogoUrl(value);
                    assertEquals(expected, league.getLogoUrl());
                }
                case "PDF" -> {
                    league.updateRulesPdfUrl(value);
                    assertEquals(expected, league.getRulesPdfUrl());
                }
            }
        }

        @Test
        @DisplayName("Should pass validation when URL is valid")
        void shouldPassWhenUrlIsValid() {
            String validUrl = "https://racecontrol.com/logo.png";
            league.updateLogoUrl(validUrl);
            assertEquals(validUrl, league.getLogoUrl());
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NAME, '', 'Name cannot be empty.'",
                "NAME, 'GT7', 'Name must be at least 5 characters long.'",
                "DESC, '', 'Description cannot be empty.'",
                "DESC, 'Short', 'Description must be at least 10 characters long.'",
                "LOGO, NULL, 'LogoUrl cannot be empty.'",
                "LOGO, 'invalid', 'The URL for LogoUrl is invalid.'",
                "LOGO, '', 'LogoUrl cannot be empty.'",
                "LOGO, 'espaço no meio.com', 'The URL for LogoUrl is invalid.'",
                "LOGO, 'http://^invalid^.com', 'The URL for LogoUrl is invalid.'",
                "LOGO, ':::::', 'The URL for LogoUrl is invalid.'",
                "PDF, NULL, 'RulesPDFUrl cannot be empty.'",
                "PDF, 'invalid', 'The URL for RulesPDFUrl is invalid.'",
                "PDF, '', 'RulesPDFUrl cannot be empty.'"
        }, nullValues = {"NULL"})
        @DisplayName("Should validate all fields on update")
        void shouldValidateFieldsOnUpdate(String field, String value, String message) {
            assertThatBusinessException(() -> {
                switch (field) {
                    case "NAME" -> league.updateName(value);
                    case "DESC" -> league.updateDescription(value);
                    case "LOGO" -> league.updateLogoUrl(value);
                    case "PDF" -> league.updateRulesPdfUrl(value);
                }
            }, message);
        }
    }

    @Nested
    @DisplayName("Configure Extra Points")
    class ConfigureExtraPoints {

        @Test
        @DisplayName("Should configure extra points correctly")
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
        @DisplayName("Should throw exception when extra points are negative")
        void shouldThrowWhenExtraPointsAreNegative(int pole, int fastest, String message) {
            assertThatBusinessException(
                    () -> league.configureExtraPoints(pole, fastest),
                    message
            );
        }

    }

}