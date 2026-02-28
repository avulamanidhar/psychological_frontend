package com.example.mindguardaipsychologicalsupportapp;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class TimeFormatUtils {
    private TimeFormatUtils() {}

    @NonNull
    public static String formatRelative(@NonNull Date date) {
        Calendar now = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        SimpleDateFormat time = new SimpleDateFormat("h:mm a", Locale.getDefault());

        if (isSameDay(now, cal)) {
            return "Today, " + time.format(date);
        }

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        if (isSameDay(yesterday, cal)) {
            return "Yesterday, " + time.format(date);
        }

        SimpleDateFormat monthDay = new SimpleDateFormat("MMM d, ", Locale.getDefault());
        return monthDay.format(date) + time.format(date);
    }

    private static boolean isSameDay(@NonNull Calendar a, @NonNull Calendar b) {
        return a.get(Calendar.ERA) == b.get(Calendar.ERA)
                && a.get(Calendar.YEAR) == b.get(Calendar.YEAR)
                && a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR);
    }
}

