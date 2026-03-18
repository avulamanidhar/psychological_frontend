package com.example.mindguardaipsychologicalsupportapp.api;

import com.example.mindguardaipsychologicalsupportapp.MoodEntry;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MindGuardApiService {

    // Onboarding & Identity
    @POST("api/users/")
    Call<okhttp3.ResponseBody> registerUser(@Body Map<String, Object> user);

    @POST("api/login/")
    Call<Map<String, Object>> login(@Body Map<String, String> credentials);

    @POST("api/forgot-password/")
    Call<Map<String, Object>> forgotPassword(@Body Map<String, String> data);

    @GET("api/profile/{username}/")
    Call<Map<String, Object>> getUserProfile(@Path("username") String username);

    @PUT("api/profile/{username}/")
    Call<Map<String, Object>> updateUserProfile(@Path("username") String username, @Body Map<String, Object> profile);

    // Mood Tracking
    @GET("api/moods/")
    Call<List<MoodEntry>> getMoodEntries(@Query("username") String username);

    @GET("api/moods/{pk}/")
    Call<MoodEntry> getMoodDetail(@Path("pk") String pk);

    @POST("api/moods/")
    Call<MoodEntry> createMoodEntry(@Query("username") String username, @Body MoodEntry moodEntry);

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

    @GET("api/activities/breathing-pattern/")
    Call<Map<String, Object>> getBreathingPattern();

    @GET("api/activities/focus-timer-config/")
    Call<Map<String, Object>> getFocusTimerConfig();

    @GET("api/activities/grounding-exercise/")
    Call<Map<String, Object>> getGroundingExerciseConfig();

    @GET("api/activities/meditation-content/")
    Call<Map<String, Object>> getMeditationContentConfig();

    @GET("api/activities/self-care-config/")
    Call<Map<String, Object>> getSelfCareConfig();

    @GET("api/activities/tools-directory/")
    Call<Map<String, Object>> getToolsDirectoryConfig();

    @GET("api/config/why-mindguard/")
    Call<Map<String, Object>> getWhyMindguardConfig();

    // Dashboard & Insights
    @GET("api/dashboard/{username}/")
    Call<Map<String, Object>> getDashboardSummary(@Path("username") String username);

    @GET("api/insights/trends/{username}/")
    Call<Map<String, Object>> getInsightTrends(@Path("username") String username);

    @GET("api/insights/patterns/{username}/")
    Call<List<Map<String, Object>>> getInsightPatterns(@Path("username") String username);

    @GET("api/insights/graph/{username}/")
    Call<Map<String, Object>> getMoodTrendGraph(@Path("username") String username);

    @GET("api/health-score/{username}/")
    Call<Map<String, Object>> getHealthScoreDetail(@Path("username") String username);

    @GET("api/health-score/{username}/")
    Call<Map<String, Object>> getMentalHealthScore(@Path("username") String username);

    @GET("api/key-indicators/{username}/")
    Call<Map<String, Map<String, Object>>> getKeyIndicators(@Path("username") String username);

    @GET("api/insights/analysis/")
    Call<List<Map<String, Object>>> getAIAnalysis();

    @GET("api/insights/transparency/")
    Call<List<Map<String, Object>>> getAiTransparency();

    @GET("api/faq/")
    Call<List<Map<String, Object>>> getFaqs();

    @GET("api/how-it-works/")
    Call<List<Map<String, Object>>> getHowItWorksSteps();

    @GET("api/system/status/")
    Call<Map<String, Object>> getSystemStatus();

    @GET("api/system/config/")
    Call<List<Map<String, Object>>> getAppConfigs();

    @GET("api/system/moods/")
    Call<List<Map<String, Object>>> getMoodTypes();

    @POST("api/feedback/")
    Call<Map<String, Object>> submitFeedback(@Body Map<String, Object> feedback);

    @GET("api/notifications/")
    Call<List<Map<String, Object>>> getNotifications();

    @PATCH("api/notifications/{pk}/")
    Call<Map<String, Object>> markNotificationAsRead(@Path("pk") int pk);

    @DELETE("api/notifications/{pk}/")
    Call<Void> deleteNotification(@Path("pk") int pk);

    @GET("api/recommendations/")
    Call<List<Map<String, Object>>> getRecommendations();

    @GET("api/privacy-policy/")
    Call<Map<String, Object>> getPrivacyPolicy();

    @POST("api/reflection-generator/")
    Call<Map<String, String>> generateReflection(@Body Map<String, Object> data);

    @GET("api/user/export/")
    Call<Map<String, Object>> exportUserData();

    @DELETE("api/user/delete-data/")
    Call<Void> deleteUserData();
}
