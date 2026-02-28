package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MoodEntryStorage {
    private static final String PREFS = "mindguard_mood_entries";
    private static final String KEY_ENTRIES = "entries_json";

    private MoodEntryStorage() {}

    @NonNull
    public static List<MoodEntry> getAll(@NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String raw = sp.getString(KEY_ENTRIES, "[]");

        List<MoodEntry> out = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(raw == null ? "[]" : raw);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.optJSONObject(i);
                if (o == null) continue;
                out.add(MoodEntry.fromJson(o));
            }
        } catch (JSONException ignored) {
        }

        Collections.sort(out, (a, b) -> Long.compare(b.timestampMillis, a.timestampMillis));
        return out;
    }

    public static void add(@NonNull Context context, @NonNull MoodEntry entry) {
        List<MoodEntry> existing = getAll(context);
        existing.add(0, entry);
        saveAll(context, existing);
    }

    @Nullable
    public static MoodEntry getById(@NonNull Context context, @NonNull String id) {
        for (MoodEntry e : getAll(context)) {
            if (e.id.equals(id)) return e;
        }
        return null;
    }

    private static void saveAll(@NonNull Context context, @NonNull List<MoodEntry> entries) {
        JSONArray arr = new JSONArray();
        for (MoodEntry e : entries) {
            try {
                arr.put(e.toJson());
            } catch (JSONException ignored) {
            }
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_ENTRIES, arr.toString())
                .apply();
    }
}

