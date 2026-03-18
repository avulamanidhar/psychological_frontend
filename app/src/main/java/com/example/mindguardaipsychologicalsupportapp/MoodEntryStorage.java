package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class MoodEntryStorage {
    private static final String PREFS = "mindguard_mood_entries";
    private static final String KEY_ENTRIES = "entries_json";

    private MoodEntryStorage() {}

    public interface MoodFetchCallback {
        void onSuccess(List<MoodEntry> entries);
        void onError(String message);
    }

    public interface MoodAddCallback {
        void onSuccess(MoodEntry entry);
        void onError(String message);
    }

    public interface MoodFetchSingleCallback {
        void onSuccess(MoodEntry entry);
        void onError(String message);
    }

    // Get all entries (Prefers local storage for speed, attempts sync in background)
    public static void getAll(@NonNull Context context, MoodFetchCallback callback) {
        List<MoodEntry> localEntries = loadFromPrefs(context);
        Collections.sort(localEntries, (a, b) -> Long.compare(b.timestampMillis, a.timestampMillis));
        callback.onSuccess(localEntries);
    }

    // Add entry (Saves to Local AND attempts to save to Server)
    public static void add(@NonNull Context context, @NonNull MoodEntry entry, MoodAddCallback callback) {
        // 1. Save Locally first so it shows up immediately
        List<MoodEntry> entries = loadFromPrefs(context);
        entries.add(entry);
        saveToPrefs(context, entries);

        // 2. Try to save to Server
        SharedPreferences settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String username = settings.getString("user_name", "User");

        MindGuardApiService api = RetrofitClient.getApiService();
        api.createMoodEntry(username, entry).enqueue(new Callback<MoodEntry>() {
            @Override
            public void onResponse(Call<MoodEntry> call, Response<MoodEntry> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    // Even if server fails, we already saved locally
                    callback.onSuccess(entry);
                }
            }

            @Override
            public void onFailure(Call<MoodEntry> call, Throwable t) {
                // Network failed, but local save is done
                callback.onSuccess(entry);
            }
        });
    }

    public static void getById(@NonNull Context context, @NonNull String id, MoodFetchSingleCallback callback) {
        List<MoodEntry> entries = loadFromPrefs(context);
        for (MoodEntry e : entries) {
            if (e.id.equals(id)) {
                callback.onSuccess(e);
                return;
            }
        }
        callback.onError("Entry not found");
    }

    private static List<MoodEntry> loadFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_ENTRIES, "[]");
        List<MoodEntry> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                list.add(MoodEntry.fromJson(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void saveToPrefs(Context context, List<MoodEntry> entries) {
        JSONArray arr = new JSONArray();
        for (MoodEntry e : entries) {
            try {
                arr.put(e.toJson());
            } catch (JSONException ignored) {}
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_ENTRIES, arr.toString())
                .apply();
    }
}
