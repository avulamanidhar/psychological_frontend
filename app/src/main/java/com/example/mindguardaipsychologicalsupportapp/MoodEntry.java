package com.example.mindguardaipsychologicalsupportapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MoodEntry {
    @NonNull public final String id;
    public final long timestampMillis;
    @NonNull public final String moodName;
    public final int moodImageResId;
    public final int intensity;
    @NonNull public final List<String> triggers;
    @NonNull public final String journal;
    @NonNull public final String aiReflection;

    public MoodEntry(
            @NonNull String id,
            long timestampMillis,
            @NonNull String moodName,
            int moodImageResId,
            int intensity,
            @NonNull List<String> triggers,
            @NonNull String journal,
            @NonNull String aiReflection
    ) {
        this.id = id;
        this.timestampMillis = timestampMillis;
        this.moodName = moodName;
        this.moodImageResId = moodImageResId;
        this.intensity = intensity;
        this.triggers = triggers;
        this.journal = journal;
        this.aiReflection = aiReflection;
    }

    @NonNull
    public static MoodEntry createNow(
            @NonNull String moodName,
            int moodImageResId,
            int intensity,
            @Nullable List<String> triggers,
            @Nullable String journal
    ) {
        List<String> safeTriggers = triggers == null ? new ArrayList<>() : new ArrayList<>(triggers);
        String safeJournal = journal == null ? "" : journal;
        String reflection = ReflectionGenerator.generate(moodName, intensity, safeTriggers);
        return new MoodEntry(
                UUID.randomUUID().toString(),
                System.currentTimeMillis(),
                moodName,
                moodImageResId,
                intensity,
                safeTriggers,
                safeJournal,
                reflection
        );
    }

    @NonNull
    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("timestampMillis", timestampMillis);
        o.put("moodName", moodName);
        o.put("moodImageResId", moodImageResId);
        o.put("intensity", intensity);
        o.put("journal", journal);
        o.put("aiReflection", aiReflection);
        JSONArray arr = new JSONArray();
        for (String t : triggers) arr.put(t);
        o.put("triggers", arr);
        return o;
    }

    @NonNull
    public static MoodEntry fromJson(@NonNull JSONObject o) throws JSONException {
        String id = o.optString("id", UUID.randomUUID().toString());
        long ts = o.optLong("timestampMillis", System.currentTimeMillis());
        String moodName = o.optString("moodName", "Okay");
        int moodImageResId = o.optInt("moodImageResId", R.drawable.img_30);
        int intensity = o.optInt("intensity", 50);
        String journal = o.optString("journal", "");
        List<String> triggers = new ArrayList<>();
        JSONArray arr = o.optJSONArray("triggers");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                String t = arr.optString(i, "").trim();
                if (!t.isEmpty()) triggers.add(t);
            }
        }
        String aiReflection = o.optString("aiReflection", ReflectionGenerator.generate(moodName, intensity, triggers));
        return new MoodEntry(id, ts, moodName, moodImageResId, intensity, triggers, journal, aiReflection);
    }
}

