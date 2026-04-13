package com.racecontrol.api.domain.model;

import com.racecontrol.api.support.builders.DriverBuilder;
import com.racecontrol.api.support.assertions.DomainAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("Should create team and validate invalid inputs")
    void shouldCreate() {
        assertEquals("Lewis Hamilton", driver.getName());
        assertAll(
                () -> assertThatBusinessException(
                        () -> DriverBuilder.driver().withBirthDate(LocalDate.now().minusYears(10)).build(),
                        "Birth date must be at least 13 years old."),
                () -> assertThatBusinessException(
                        () -> DriverBuilder.driver().withBirthDate(null).build(),
                        "Birth date is mandatory.")
        );
    }

    @Nested
    @DisplayName("Update Name workflow")
    class UpdateName {
        @Test
        void shouldUpdateName() {
            assertUpdateWorkflow(
                    driver::updateName, driver::getName,
                    "   Max   Verstappen  ", "Max Verstappen"
            );
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> driver.updateName("Lewis Hamilton"), driver::getName);
        }
    }

    @Nested
    @DisplayName("Update GamerTag workflow")
    class UpdateGamerTag {
        @Test
        void shouldUpdateGamerTag() {
            assertUpdateWorkflow(
                    driver::updateGamerTag, driver::getGamerTag,
                    "   MaxVerstappen33  ", "MaxVerstappen33"
            );
        }

        @Test
        void shouldIgnoreSameValue() {
            assertNoChange(() -> driver.updateGamerTag("xX_LewisHamilton_Xx"), driver::getGamerTag);
        }
    }

    @Test
    @DisplayName("Should calculate age correctly")
    void shouldCalculateAge() {
        assertEquals(26, driver.getAge(fixedClock));
    }

}
