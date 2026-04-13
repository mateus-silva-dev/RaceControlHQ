package com.racecontrol.api.domain.model;

import lombok.Getter;

@Getter
public class TestEntity extends BaseEntity {

    private String name;

    public void updateName(String name) {
        updateField(name, this.name, value -> this.name = value);
    }

}
