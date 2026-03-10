package com.example.mindguardaipsychologicalsupportapp.api;

import com.example.mindguardaipsychologicalsupportapp.MoodEntry;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MindGuardApiService {

    // Onboarding & Identity
    @POST("api/users/")
    Call<ResponseBody> registerUser(@Body Map<String, String> user);

    @GET("api/profile/{username}/")
    Call<Map<String, Object>> getUserProfile(@Path("username") String username);

    @PUT("api/profile/{username}/")
    Call<Map<String, Object>> updateUserProfile(@Path("username") String username, @Body Map<String, Object> profile);

    // Mood Tracking
    @GET("api/moods/")
    Call<List<MoodEntry>> getMoodEntries(@Query("username") String username);

    @GET("api/moods/{id}/")
    Call<MoodEntry> getMoodEntry(@Path("id") String id);

    @POST("api/moods/")
    Call<MoodEntry> createMoodEntry(@Body MoodEntry moodEntry);

    // AI Therapeutic Chat
    @GET("api/chat/")
    Call<List<Map<String, Object>>> getChatHistory(@Query("username") String username);

    @POST("api/chat/")
    Call<Map<String, Object>> sendChatMessage(@Body Map<String, Object> message);

    // Well-being Tools
    @GET("api/activities/")
    Call<List<Map<String, Object>>> getActivityLogs(@Query("username") String username);

    @POST("api/activities/")
    Call<Map<String, Object>> logActivity(@Body Map<String, Object> activity);

    // Dashboard & Insights
    @GET("api/dashboard/{username}/")
    Call<Map<String, Object>> getDashboardSummary(@Path("username") String username);

    @GET("api/insights/trends/{username}/")
    Call<Map<String, Object>> getInsightTrends(@Path("username") String username);

    @GET("api/insights/patterns/{username}/")
    Call<List<Map<String, Object>>> getInsightPatterns(@Path("username") String username);

    @GET("api/health-score/{username}/")
    Call<Map<String, Object>> getHealthScoreDetail(@Path("username") String username);

    @POST("api/feedback/")
    Call<Map<String, Object>> submitFeedback(@Body Map<String, Object> feedback);
}
