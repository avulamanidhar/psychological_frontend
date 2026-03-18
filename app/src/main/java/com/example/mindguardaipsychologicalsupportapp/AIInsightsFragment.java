package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AIInsightsFragment extends Fragment {

    private TextView txtWeeklyScore, txtTrendComparison;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ai_insights, container, false);

        txtWeeklyScore = view.findViewById(R.id.txtWeeklyScore);
        txtTrendComparison = view.findViewById(R.id.txtTrendComparison);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Navigation.findNavController(view).navigateUp();
            });
        }

        setupNavigation(view);
        loadInsightSummary();

        return view;
    }

    private void setupNavigation(View view) {
        View cardMoodTrends = view.findViewById(R.id.cardMoodTrends);
        if (cardMoodTrends != null) {
            cardMoodTrends.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_aiInsightsFragment_to_moodTrendsFragment);
            });
        }

        View cardKeyIndicators = view.findViewById(R.id.cardKeyIndicators);
        if (cardKeyIndicators != null) {
            cardKeyIndicators.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_aiInsightsFragment_to_keyIndicatorsFragment);
            });
        }

        View cardPatterns = view.findViewById(R.id.cardPatterns);
        if (cardPatterns != null) {
            cardPatterns.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_aiInsightsFragment_to_detectedPatternsFragment);
            });
        }

        View cardCompare = view.findViewById(R.id.cardCompare);
        if (cardCompare != null) {
            cardCompare.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_aiInsightsFragment_to_todayVsLastWeekFragment);
            });
        }

        View cardRecommendations = view.findViewById(R.id.cardRecommendations);
        if (cardRecommendations != null) {
            cardRecommendations.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_aiInsightsFragment_to_personalizedRecommendationsFragment);
            });
        }

        View cardAIAnalysis = view.findViewById(R.id.cardAIAnalysis);
        if (cardAIAnalysis != null) {
            cardAIAnalysis.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_aiInsightsFragment_to_aiAnalysisFragment);
            });
        }
    }

    private void loadInsightSummary() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        MindGuardApiService api = RetrofitClient.getApiService();
        api.getInsightTrends(userName).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    
                    if (txtWeeklyScore != null && data.containsKey("weekly_summary_score")) {
                        Object scoreObj = data.get("weekly_summary_score");
                        int score = scoreObj instanceof Double ? ((Double) scoreObj).intValue() : (int) scoreObj;
                        txtWeeklyScore.setText(String.valueOf(score));
                    }
                    
                    if (txtTrendComparison != null && data.containsKey("trend_text")) {
                        txtTrendComparison.setText((String) data.get("trend_text"));
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                // Silently fail
            }
        });
    }
}