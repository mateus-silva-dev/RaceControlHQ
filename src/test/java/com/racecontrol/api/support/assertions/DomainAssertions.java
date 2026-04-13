package com.racecontrol.api.support.assertions;

import com.racecontrol.api.core.exception.BusinessRuleException;
import org.assertj.core.api.ThrowableAssert;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public interface DomainAssertions {

    default void assertThatBusinessException(ThrowableAssert.ThrowingCallable action, String expectedMessage) {
        assertThatThrownBy(action).isInstanceOf(BusinessRuleException.class).hasMessage(expectedMessage);
    }

    default <T> void assertUpdateWorkflow(Consumer<T> updateMethod, Supplier<T> getterMethod, T newValue, T expectedValue) {
        assertIdempotent(() -> updateMethod.accept(newValue), getterMethod);
        assertEquals(expectedValue, getterMethod.get());
    }

    default void assertNoChange(Runnable action, Supplier<?>... getters) {
        Object[] before = captureState(getters);
        action.run();
        Object[] after = captureState(getters);
        assertThat(after)
                .as("State should not change")
                .containsExactly(before);
    }

    default void assertIdempotent(Runnable action, Supplier<?>... getters) {
        action.run();
        Object[] afterFirst = captureState(getters);
        action.run();
        Object[] afterSecond = captureState(getters);
        assertThat(afterSecond)
                .as("Second execution should not change state")
                .containsExactly(afterFirst);
    }

    private Object[] captureState(Supplier<?>... getters) {
        return Arrays.stream(getters)
                .map(Supplier::get)
                .toArray();
    }

}
