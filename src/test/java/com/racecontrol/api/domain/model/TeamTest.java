package com.racecontrol.api.model;

import com.racecontrol.api.support.builders.TeamBuilder;
import com.racecontrol.api.domain.model.Team;
import com.racecontrol.api.domain.model.valueObject.HexColor;
import com.racecontrol.api.support.assertions.DomainAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Team")
public class TeamTest implements DomainAssertions {

    private Team team;

    @BeforeEach
    void setUp() {
        team = TeamBuilder.team().build();
    }

    @Nested
    @DisplayName("Creation Scenarios")
    class TeamCreation {

        @Test
        void shouldCreateSuccessfully() {
            assertAll(
                    () -> assertEquals("Mercedes-AMG Motorsports", team.getName()),
                    () -> assertEquals(new HexColor("#00A19B"), team.getHexColor()),
                    () -> assertEquals("Mercedes-AMG GT3", team.getCar_manufacturer()),
                    () -> assertTrue(team.isActive())
            );
        }

        @Test
        @DisplayName("Should throw exception for invalid inputs during creation")
        void shouldValidateHexColor() {
            assertThatBusinessException(() -> {
                TeamBuilder.team().withHexColor(new HexColor("#12345GG")).build();
            }, "Invalid HEX color.");
        }

        @ParameterizedTest
        @CsvSource(value = {
                "'Mercedes-AMG Motorsports', '#00A19B', NULL, 'Car manufacturer cannot be empty.'",
                "'Mercedes-AMG Motorsports', '#00A19B', 'M', 'Car manufacturer must be at least 2 characters long.'"
        }, nullValues = {"NULL"})
        void shouldValidateCarManufacturer(String name, String msg) {
            assertThatBusinessException(() -> TeamBuilder.team().withCarManufacturer(name).build(), msg);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "NULL, 'Name cannot be empty.'",
                "'VG', 'Name must be at least 3 characters long.'"
        }, nullValues = {"NULL"})
        void shouldValidateName(String name, String msg) {
            assertThatBusinessException(() -> TeamBuilder.team().withName(name).build(), msg);
        }

    }

    @Nested
    @DisplayName("Update Scenarios")
    class TeamUpdate {

            @Test
            void shouldUpdateName() {
                assertUpdateWorkflow(team::updateName, team::getName, "    Red  Bull   Racing  ", "Red Bull Racing");
            }

            @Test
            @DisplayName("Should update HexColor")
            void shouldUpdateHexColor() {
                HexColor expected = new HexColor("#565F64");
                assertUpdateWorkflow(team::updateHexColor, team::getHexColor, new HexColor("   #565F64   "), expected);
            }

            @Test
            @DisplayName("Should update car manufacturer")
            void shouldUpdateCarManufacturer() {
                assertUpdateWorkflow(team::updateCarManufacturer, team::getCar_manufacturer, "   Ferrari   ", "Ferrari");
            }

            @Test
            void shouldUpdateLogoUrl() {
                assertUpdateWorkflow(team::updateLogoUrl, team::getLogo_url,
                        "https://cdn.worldvectorlogo.com/logos/ferrari-ges.svg",
                        "https://cdn.worldvectorlogo.com/logos/ferrari-ges.svg");
            }

//        @ParameterizedTest
//        @CsvSource(value = {
//                "NAME, '', 'Team name cannot be empty.'",
//                "NAME, '   ', 'Team name cannot be empty.'",
//                "NAME, 'Me', 'Team name must be at least 3 characters long.'",
//
//                "HEXCOLOR, '#12345GG', 'Invalid HEX color.'",
//                "HEXCOLOR, 'azul', 'Invalid HEX color.'",
//                "HEXCOLOR, '12345', 'Invalid HEX color.'",
//
//                "'CAR', NULL, 'Car manufacturer cannot be empty.'",
//                "'CAR', '', 'Car manufacturer cannot be empty.'",
//                "'CAR', '   ', 'Car manufacturer cannot be empty.'",
//                "'CAR', 'M', 'Car manufacturer must be at least 2 characters long.'",
//
//                "LOGO, NULL, 'LogoTeamUrl cannot be empty.'",
//                "LOGO, 'invalid', 'The URL for LogoTeamUrl is invalid.'",
//                "LOGO, '', 'LogoTeamUrl cannot be empty.'",
//                "LOGO, 'espaço no meio.com', 'The URL for LogoTeamUrl is invalid.'",
//                "LOGO, 'http://^invalid^.com', 'The URL for LogoTeamUrl is invalid.'",
//                "LOGO, ':::::', 'The URL for LogoTeamUrl is invalid.'",
//        }, nullValues = {"NULL"})
//        @DisplayName("Should validate all fields on update")
//        void shouldUpdateFields() {
//            assertThatBusinessException(() -> {
//                switch (field) {
//                    case "NAME" -> team.updateName(value);
//                    case "HEXCOLOR" -> team.updateHexColor(new HexColor(value));
//                    case "CAR" -> team.updateCarManufacturer(value);
//                    case "LOGO" -> team.updateLogoUrl(value);
//                    default -> throw new IllegalArgumentException("Unknown field: " + field);
//                }
//            }, message);
        }
    }


