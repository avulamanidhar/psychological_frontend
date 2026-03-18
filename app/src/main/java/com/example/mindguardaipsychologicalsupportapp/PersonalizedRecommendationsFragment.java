package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalizedRecommendationsFragment extends Fragment {

    private LinearLayout recommendationsContainer;
    private MindGuardApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personalized_recommendations, container, false);

        apiService = RetrofitClient.getApiService();
        recommendationsContainer = view.findViewById(R.id.recommendationsContainer);

        fetchRecommendations();

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        return view;
    }

    private void fetchRecommendations() {
        apiService.getRecommendations().enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    renderRecommendations(response.body());
                } else {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Failed to load recommendations", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void renderRecommendations(List<Map<String, Object>> recommendations) {
        if (recommendationsContainer == null || !isAdded()) return;
        recommendationsContainer.removeAllViews();

        for (Map<String, Object> rec : recommendations) {
            View itemView = getLayoutInflater().inflate(R.layout.item_recommendation, recommendationsContainer, false);
            
            TextView title = itemView.findViewById(R.id.txtRecTitle);
            TextView desc = itemView.findViewById(R.id.txtRecDescription);
            TextView duration = itemView.findViewById(R.id.txtRecDuration);
            TextView difficulty = itemView.findViewById(R.id.txtRecDifficulty);
            TextView action = itemView.findViewById(R.id.txtRecAction);
            ImageView img = itemView.findViewById(R.id.imgRecommendation);
            MaterialCardView difficultyCard = itemView.findViewById(R.id.cardDifficulty);

            if (title != null) title.setText((String) rec.get("title"));
            if (desc != null) desc.setText((String) rec.get("description"));
            if (duration != null) duration.setText("🕒 " + rec.get("duration"));
            if (difficulty != null) difficulty.setText((String) rec.get("difficulty"));
            if (action != null) action.setText((String) rec.get("action_text"));

            // Set Image Resource based on tag
            String imgTag = (String) rec.get("image_tag");
            if (imgTag != null && img != null) {
                int resId = getResources().getIdentifier(imgTag, "drawable", requireContext().getPackageName());
                if (resId != 0) {
                    img.setImageResource(resId);
                }
            }

            // Style Difficulty
            if (difficultyCard != null && difficulty != null) {
                String diff = (String) rec.get("difficulty");
                if ("Easy".equalsIgnoreCase(diff)) {
                    difficultyCard.setCardBackgroundColor(0xFFF0FDF4);
                    difficulty.setTextColor(0xFF4CAF50);
                } else {
                    difficultyCard.setCardBackgroundColor(0xFFFFF7ED);
                    difficulty.setTextColor(0xFFF59E0B);
                }
            }

            // Click handling
            String type = (String) rec.get("type");
            itemView.setOnClickListener(v -> {
                if ("Breathing".equalsIgnoreCase(type)) {
                    Navigation.findNavController(v).navigate(R.id.toolBreathingFragment);
                } else if ("Meditation".equalsIgnoreCase(type)) {
                    Navigation.findNavController(v).navigate(R.id.toolMeditationFragment);
                } else if ("Journaling".equalsIgnoreCase(type)) {
                    Navigation.findNavController(v).navigate(R.id.moodThoughtsFragment);
                } else if ("Movement".equalsIgnoreCase(type)) {
                    Toast.makeText(getContext(), "Starting " + type, Toast.LENGTH_SHORT).show();
                }
            });

            recommendationsContainer.addView(itemView);
        }
    }
}