package com.racecontrol.api.domain.util;

import java.text.Normalizer;

public class Slugifier {

    public static String generate(String text) {
        String normalized = Normalizer
                .normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        return normalized
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }

}
