package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class AIInsightsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ai_insights, container, false);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Navigation.findNavController(view).navigateUp();
            });
        }

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

        return view;
    }
}