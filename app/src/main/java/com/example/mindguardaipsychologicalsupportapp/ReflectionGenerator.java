package com.example.mindguardaipsychologicalsupportapp;

import androidx.annotation.NonNull;

import java.util.List;

public final class ReflectionGenerator {
    private ReflectionGenerator() {}

    @NonNull
    public static String generate(@NonNull String moodName, int intensity, @NonNull List<String> triggers) {
        String intensityLabel;
        if (intensity < 30) intensityLabel = "low";
        else if (intensity < 70) intensityLabel = "moderate";
        else intensityLabel = "high";

        StringBuilder sb = new StringBuilder();
        sb.append("Based on this entry, you were experiencing ")
                .append(moodName.toLowerCase())
                .append(" emotions with ")
                .append(intensityLabel)
                .append(" intensity.");

        if (triggers.isEmpty()) {
            sb.append(" No specific triggers were identified.");
        } else if (triggers.size() == 1) {
            sb.append(" ").append(triggers.get(0)).append(" appear to be contributing factors.");
        } else {
            sb.append(" ");
            for (int i = 0; i < triggers.size(); i++) {
                if (i == 0) sb.append(triggers.get(i));
                else if (i == triggers.size() - 1) sb.append(" and ").append(triggers.get(i));
                else sb.append(", ").append(triggers.get(i));
            }
            sb.append(" appear to be contributing factors.");
        }

        return sb.toString();
    }
}

