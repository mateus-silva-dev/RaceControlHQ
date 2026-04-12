package com.racecontrol.api.domain.model.valueObject;

import com.racecontrol.api.core.exception.BusinessRuleException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "value")
public class HexColor {

    @Column(name = "hex_color", nullable = false, length = 7)
    private String value;

    public HexColor(String value) {
        String normalized = normalize(value);

        if (!isValid(normalized)) {
            throw new BusinessRuleException("Invalid HEX color");
        }

        this.value = normalized;
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (!trimmed.startsWith("#")) {
            trimmed = "#" + trimmed;
        }
        return trimmed.toUpperCase();
    }

    private boolean isValid(String value) {
        return value != null && value.matches("^#([A-F0-9]{6}|[A-F0-9]{3})$");
    }
}
