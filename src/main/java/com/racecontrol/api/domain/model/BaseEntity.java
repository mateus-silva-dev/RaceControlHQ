package com.racecontrol.api.domain.model;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class BaseEntity {

    protected <T> void updateField(T newValue, T currentValue, Consumer<T> updater) {
        if (isUnchanged(newValue, currentValue)) return;
        updater.accept(newValue);
    }

    protected <T> boolean isUnchanged(T newValue, T currentValue) {
        return Objects.equals(newValue, currentValue);
    }
}
