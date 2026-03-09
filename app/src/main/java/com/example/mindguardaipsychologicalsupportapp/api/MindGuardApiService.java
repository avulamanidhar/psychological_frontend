package com.example.mindguardaipsychologicalsupportapp.api;

import com.example.mindguardaipsychologicalsupportapp.MoodEntry;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MindGuardApiService {

    @GET("api/moods/")
    Call<List<MoodEntry>> getMoodEntries();

    @GET("api/moods/{id}/")
    Call<MoodEntry> getMoodEntry(@Path("id") String id);

    @POST("api/moods/")
    Call<MoodEntry> createMoodEntry(@Body MoodEntry moodEntry);
}
