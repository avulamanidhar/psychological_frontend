package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.Resources;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public final class MoodUi {
    private MoodUi() {}

    @ColorInt
    public static int colorForMood(@NonNull Resources res, @NonNull String moodName) {
        String m = moodName.trim().toLowerCase();
        switch (m) {
            case "great":
                return Color.parseColor("#F6A623"); // orange-ish
            case "good":
                return res.getColor(R.color.tag_green_text);
            case "okay":
            case "low":
            case "tired":
                return Color.parseColor("#9E9E9E");
            case "sad":
                return Color.parseColor("#7E57C2");
            case "anxious":
            case "frustrated":
                return res.getColor(R.color.line_anxiety);
            default:
                return res.getColor(R.color.button_blue);
        }
    }

    @ColorInt
    public static int cardBgForMood(@NonNull String moodName) {
        String m = moodName.trim().toLowerCase();
        switch (m) {
            case "great":
            case "good":
                return Color.parseColor("#FFF9C4"); // light yellow
            case "anxious":
            case "sad":
            case "frustrated":
                return Color.parseColor("#F3E5F5"); // light purple
            default:
                return Color.parseColor("#E8F5E9"); // light green-ish
        }
    }
}

