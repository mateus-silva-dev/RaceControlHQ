package com.racecontrol.api.league;

import com.racecontrol.api.core.code.Code;
import com.racecontrol.api.core.exception.BusinessRuleException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "league")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 110, unique = true)
    private String slugify;

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
        this.name = validateAndNormalize(name, "Name", 5);
        this.description = validateAndNormalize(description, "Description", 10);
        this.slugify = generateSlugify(this.name);
        this.logoUrl = null;
        this.rulesPdfUrl = null;
    }

    public static League create(String name, String description) {
        return new League(name, description);
    }


    public void updateName(String newName) {
        String name = validateAndNormalize(newName, "Name", 5);
        if (isUnchanged(name, this.name)) return;
        this.name = name;
        this.slugify = generateSlugify(name);
    }

    public void updateDescription(String newDescription) {
        String description = validateAndNormalize(newDescription, "Description", 10);
        if (isUnchanged(description, this.description)) return;
        this.description = description;
    }

    public void updateLogoUrl(String newLogoUrl) {
        String logoUrl = validateUrlStructure(newLogoUrl, "LogoUrl");
        if (isUnchanged(logoUrl, this.logoUrl)) return;
        this.logoUrl = logoUrl;
    }

    public void updateRulesPdfUrl(String newRulesPdfUrl) {
        String pdfUrl = validateUrlStructure(newRulesPdfUrl, "RulesPDFUrl");
        if (isUnchanged(pdfUrl, this.rulesPdfUrl)) return;
        this.rulesPdfUrl = pdfUrl;
    }

    public void configureExtraPoints(int pointsPole, int pointsFastestLap) {
        if (pointsPole < 0 || pointsFastestLap < 0) {
            throw new BusinessRuleException("Points cannot be negative.");
        }
        this.pointsPole = pointsPole;
        this.pointsFastestLap = pointsFastestLap;
    }


    private String validateAndNormalize(String text, String fieldName, int min) {
        if (text == null) {
            throw new BusinessRuleException(fieldName + " cannot be null.");
        }
        String value = text.trim().replaceAll("\\s+", " ");
        if (value.isEmpty()) {
            throw new BusinessRuleException(fieldName + " cannot be empty.");
        }
        if (value.trim().length() < min) {
            throw new BusinessRuleException(fieldName + " must be at least " + min + " characters long.");
        }
        return value;
    }

    private String generateSlugify(String text) {
        String normalized = Normalizer
                .normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        return normalized
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }

    private String validateUrlStructure(String url, String fieldName) {
        if (url == null) {
            return null;
        }
        if (url.isBlank()) {
            throw new BusinessRuleException("URL cannot be empty.", Code.INVALID_URL);
        }
        String normalized = url.trim().toLowerCase();
        if (!normalized.startsWith("http://") && !normalized.startsWith("https://")) {
            normalized = "https://" + normalized;
        }
        if (!normalized.contains(".")) {
            throw new BusinessRuleException("The URL for " + fieldName + " is invalid.", Code.INVALID_URL);
        }
        return normalized;
    }

    private <T> boolean isUnchanged(T newValue, T currentValue) {
        return Objects.equals(newValue, currentValue);
    }
}
