package com.racecontrol.api.domain.model;

import com.racecontrol.api.core.exception.BusinessRuleException;
import com.racecontrol.api.domain.util.Slugifier;
import com.racecontrol.api.domain.validation.TextValidation;
import com.racecontrol.api.domain.validation.UrlValidation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "league")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false, of = "id")
public class League extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 110, unique = true)
    private String slug;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "rules_pdf_url")
    private String rulesPdfUrl;

    @Column(name = "points_pole")
    private int pointsPole = 0;

    @Column(name = "points_fastest_lap")
    private int pointsFastestLap = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public League(String name, String description) {
        this.name = TextValidation.validate(name, "Name", 5);
        this.description = TextValidation.validate(description, "Description", 10);
        this.slug = Slugifier.generate(this.name);
    }

    public static League create(String name, String description) {
        return new League(name, description);
    }


    public void updateName(String newName) {
        String name = TextValidation.validate(newName, "Name", 5);
        updateField(name, this.name, value -> {
            this.name = value;
            this.slug = Slugifier.generate(value);
        });
    }

    public void updateDescription(String newDescription) {
        String description = TextValidation.validate(newDescription, "Description", 10);
        updateField(description, this.description, value -> {
            this.description = value;
        });
    }

    public void updateLogoUrl(String newLogoUrl) {
        String logoUrl = UrlValidation.validateUrl(newLogoUrl, "LogoUrl");
        updateField(logoUrl, this.logoUrl, value -> {
            this.logoUrl = value;
        });
    }

    public void updateRulesPdfUrl(String newRulesPdfUrl) {
        String pdfUrl = UrlValidation.validateUrl(newRulesPdfUrl, "RulesPDFUrl");
        updateField(pdfUrl, this.rulesPdfUrl, value -> {
            this.rulesPdfUrl = value;
        });
    }

    public void configureExtraPoints(int pointsPole, int pointsFastestLap) {
        if (pointsPole < 0 || pointsFastestLap < 0) {
            throw new BusinessRuleException("Points cannot be negative.");
        }
        this.pointsPole = pointsPole;
        this.pointsFastestLap = pointsFastestLap;
    }

}
