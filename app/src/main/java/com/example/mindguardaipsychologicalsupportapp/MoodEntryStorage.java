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

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;

public final class MoodEntryStorage {
    private static final String PREFS = "mindguard_mood_entries";
    private static final String KEY_ENTRIES = "entries_json";

    private MoodEntryStorage() {}

    public interface MoodFetchCallback {
        void onSuccess(List<MoodEntry> entries);
        void onError(String message);
    }

    public static void getAll(@NonNull Context context, MoodFetchCallback callback) {
        MindGuardApiService api = RetrofitClient.getApiService();
        api.getMoodEntries().enqueue(new retrofit2.Callback<List<MoodEntry>>() {
            @Override
            public void onResponse(retrofit2.Call<List<MoodEntry>> call, retrofit2.Response<List<MoodEntry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MoodEntry> out = response.body();
                    Collections.sort(out, (a, b) -> Long.compare(b.timestampMillis, a.timestampMillis));
                    callback.onSuccess(out);
                } else {
                    callback.onError("Failed to load entries: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<MoodEntry>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface MoodAddCallback {
        void onSuccess(MoodEntry entry);
        void onError(String message);
    }

    public static void add(@NonNull Context context, @NonNull MoodEntry entry, MoodAddCallback callback) {
        MindGuardApiService api = RetrofitClient.getApiService();
        api.createMoodEntry(entry).enqueue(new retrofit2.Callback<MoodEntry>() {
            @Override
            public void onResponse(retrofit2.Call<MoodEntry> call, retrofit2.Response<MoodEntry> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to add entry: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MoodEntry> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface MoodFetchSingleCallback {
        void onSuccess(MoodEntry entry);
        void onError(String message);
    }

    public static void getById(@NonNull Context context, @NonNull String id, MoodFetchSingleCallback callback) {
        MindGuardApiService api = RetrofitClient.getApiService();
        api.getMoodEntry(id).enqueue(new retrofit2.Callback<MoodEntry>() {
            @Override
            public void onResponse(retrofit2.Call<MoodEntry> call, retrofit2.Response<MoodEntry> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch entry: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MoodEntry> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
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

