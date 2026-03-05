package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class PersonalizedRecommendationsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personalized_recommendations, container, false);

        View breathingCard = view.findViewById(R.id.cardBreathingRecommendation);
        if (breathingCard != null) {
            breathingCard.setOnClickListener(v -> 
                Navigation.findNavController(view).navigate(R.id.action_personalizedRecommendationsFragment_to_toolBreathingFragment)
            );
        }

        View meditationCard = view.findViewById(R.id.cardMeditationRecommendation);
        if (meditationCard != null) {
            meditationCard.setOnClickListener(v -> 
                Navigation.findNavController(view).navigate(R.id.action_personalizedRecommendationsFragment_to_toolMeditationFragment)
            );
        }

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> 
                Navigation.findNavController(view).navigateUp()
            );
        }

        return view;
    }
}