package com.racecontrol.api.domain.model;

import com.racecontrol.api.domain.model.enums.SeasonStatus;
import com.racecontrol.api.support.assertions.DomainAssertions;
import com.racecontrol.api.support.builders.ContractBuilder;
import com.racecontrol.api.support.builders.DriverBuilder;
import com.racecontrol.api.support.builders.SeasonBuilder;
import com.racecontrol.api.support.builders.TeamBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Contract")
public class ContractTest implements DomainAssertions {

    private Contract contract;
    private Driver driver;
    private Team team;
    private Season season;

    @BeforeEach
    void setUp() {
        contract = ContractBuilder.contract().build();
        driver = DriverBuilder.driver().build();
        team = TeamBuilder.team().build();
        season = SeasonBuilder.season().build();
    }

    @Test
    @DisplayName("Should create contract and guard integrity")
    void shouldCreate() {
        Contract contract = Contract.create(driver, team, season, false);
        assertAll(
                () -> assertEquals(driver, contract.getDriver()),
                () -> assertTrue(contract.isActive()),
                () -> assertFalse(contract.isPrimaryDriver()));
    }

    @Test
    @DisplayName("Should block creation for finished seasons")
    void shouldThrowWhenSeasonFinished() {
        Season season = SeasonBuilder.season().withStatus(SeasonStatus.FINISHED).build();
        assertThatBusinessException(
                () -> ContractBuilder.contract().withSeason(season).build(),
                "Cannot create contracts for a finished season."
        );
    }

    @Nested
    @DisplayName("Contract Actions")
    class ContractActions {
        @Test
        void shouldPromoteToPrimary() {
            Contract reserveContract = ContractBuilder.contract().withPrimaryDriver(false).build();
            reserveContract.promoteToPrimary();
            assertTrue(reserveContract.isPrimaryDriver());
        }

        @Test
        void shouldThrowIfAlreadyPrimary() {
            Contract primaryContract = ContractBuilder.contract().withPrimaryDriver(true).build();
            assertThatBusinessException(
                    primaryContract::promoteToPrimary,
                    "Driver is already a primary driver."
            );
        }

        @Test
        void shouldTerminateContract() {
            contract.terminate();
            assertFalse(contract.isActive());
        }
    }

    @Test
    @DisplayName("Should identify if contract belongs to a specific season")
    void shouldIdentifySeason() {
        UUID correctId = contract.getSeason().getId();
        UUID wrongId = UUID.randomUUID();
        assertAll(
                () -> assertTrue(contract.isFromSeason(correctId)),
                () -> assertFalse(contract.isFromSeason(wrongId))
        );
    }
}