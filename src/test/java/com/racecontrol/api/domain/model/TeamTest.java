package com.racecontrol.api.domain.model;

import com.racecontrol.api.domain.valueObject.HexColor;
import com.racecontrol.api.support.builders.TeamBuilder;
import com.racecontrol.api.support.assertions.DomainAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Team")
public class TeamTest implements DomainAssertions {

    private Team team;

    @BeforeEach
    void setUp() {
        team = TeamBuilder.team().build();
    }

    @Test
    @DisplayName("Should create team and validate invalid inputs")
    void shouldCreate() {
        assertEquals("Mercedes-AMG Motorsports", team.getName());
        assertAll(
                () -> assertThatBusinessException(
                        () -> TeamBuilder.team().withName("").build(),
                        "Team name cannot be empty."),
                () -> assertThatBusinessException(
                        () -> TeamBuilder.team().withCarManufacturer(null).build(),
                        "Car manufacturer cannot be empty.")
        );
    }

    @Nested
    @DisplayName("Update Name workflow")
    class UpdateName {
        @Test
        void shouldUpdateName() {
            assertUpdateWorkflow(
                    team::updateName, team::getName,
                    "   Red    Bull    Racing  ", "Red Bull Racing");
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> team.updateName("Mercedes-AMG Motorsports"), team::getName);
        }
    }

    @Nested
    @DisplayName("Update HexColor workflow")
    class updateHexColor {
        @Test
        void shouldUpdateHexColor() {
            HexColor expected = new HexColor("#C8CCCE");
            assertUpdateWorkflow(
                    team::updateHexColor, team::getHexColor,
                    new HexColor("#C8CCCE"), expected);
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> team.updateHexColor(new HexColor("#00A19B")), team::getHexColor);
        }
    }

    @Nested
    @DisplayName("Update Car Manufacturer workflow")
    class updateCarManufacturer {
        @Test
        void shouldUpdateCarManufacturer() {
            assertUpdateWorkflow(
                    team::updateCarManufacturer, team::getCar_manufacturer,
                    "  Ferrari   Gr.4  ", "Ferrari Gr.4");
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> team.updateCarManufacturer("Mercedes-AMG GT3"), team::getCar_manufacturer);
        }
    }

    @Nested
    @DisplayName("Update Logo workflow")
    class updateLogo {
        @Test
        void shouldUpdateLogo() {
            assertUpdateWorkflow(
                    team::updateLogoUrl, team::getLogo_url,
                    "google.com/mercedes_logo.png", "https://google.com/mercedes_logo.png");
        }
    }

}