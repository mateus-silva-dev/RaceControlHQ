package com.racecontrol.api.model;

import com.racecontrol.api.support.assertions.DomainAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Base Entity")
public class BaseEntityTest implements DomainAssertions {

    private TestEntity entity;

    @BeforeEach
    void setUp() {
        entity = new TestEntity();
        entity.updateName("Original");
    }

    @Test
    @DisplayName("Should update field when value is different")
    void shouldUpdateFieldWhenDifferent() {
        entity.updateName("Changed");
        assertEquals("Changed", entity.getName());
    }

    @Test
    @DisplayName("Should not trigger update when value is the same")
    void shouldNotUpdateWhenSame() {
        assertNoChange(() -> entity.updateName("Original"), entity::getName);
    }

    @Test
    @DisplayName("Update should remain idempotent")
    void shouldBeIdempotent() {
        assertIdempotent(() -> entity.updateName("Changed"), entity::getName);
    }

}
