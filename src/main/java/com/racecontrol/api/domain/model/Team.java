package com.racecontrol.api.domain.model;

import com.racecontrol.api.domain.model.valueObject.HexColor;
import com.racecontrol.api.domain.validation.CommonValidation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "team")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "id")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 7)
    private HexColor hexColor;

    @Column(nullable = false, length = 100)
    private String car_manufacturer;

    private String logo_url;

    @Column(nullable = false)
    private boolean active;

    public Team(String name, HexColor hexColor, String car_manufacturer) {
        this.name = CommonValidation.requiredText(name, "Team name", 3);
        this.hexColor = hexColor;
        this.car_manufacturer = CommonValidation.requiredText(car_manufacturer, "Car manufacturer", 3);
        this.active = true;
    }

    public static Team create(String name, HexColor hexColor, String car_manufacturer) {
        return new Team(name, hexColor, car_manufacturer);
    }


    public void updateName(String newName) {
        String name = CommonValidation.requiredText(newName, "Team name", 3);
        updateField(name, this.name, value -> {
            this.name = value;
        });
    }

    public void updateHexColor(HexColor newHexColor) {
        updateField(newHexColor, this.hexColor, value -> {
            this.hexColor = value;
        });
    }

    public void updateCarManufacturer(String newCarManufacturer) {
        String car_manufacturer = CommonValidation.requiredText(newCarManufacturer, "Car manufacturer", 3);
        updateField(car_manufacturer, this.car_manufacturer, value -> {
            this.car_manufacturer = value;
        });
    }

    public void updateLogoUrl(String newLogoUrl) {
        updateField(newLogoUrl, this.logo_url, value -> {
            this.logo_url = value;
        });
    }

}
