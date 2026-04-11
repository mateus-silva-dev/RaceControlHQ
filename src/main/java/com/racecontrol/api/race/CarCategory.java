package com.racecontrol.api.race;

import lombok.Getter;

@Getter
public enum CarCategory {
    GR1("Gr.1"),
    GR2("Gr.2"),
    GR3("Gr.3"),
    GR4("Gr.4"),
    GRB("Gr.B"),
    GRC("Gr.C"),
    VGT("VGT"),
    ROAD_CAR("Road Car"),;

    private final String description;

    CarCategory(String description) {
        this.description = description;
    }
}
