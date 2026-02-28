package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ToolsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools, container, false);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        }

        View cardMeditation = view.findViewById(R.id.cardMeditation);
        if (cardMeditation != null) {
            cardMeditation.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_toolMeditationFragment));
        }
        View cardGrounding = view.findViewById(R.id.cardGrounding);
        if (cardGrounding != null) {
            cardGrounding.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_toolGroundingFragment));
        }
        View cardBreathing = view.findViewById(R.id.cardBreathing);
        if (cardBreathing != null) {
            cardBreathing.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_toolCompleteFragment));
        }
        View cardFocusTimer = view.findViewById(R.id.cardFocusTimer);
        if (cardFocusTimer != null) {
            cardFocusTimer.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_toolCompleteFragment));
        }
        View cardSelfCare = view.findViewById(R.id.cardSelfCare);
        if (cardSelfCare != null) {
            cardSelfCare.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_toolCompleteFragment));
        }

        // Bottom Navigation Listeners
        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_homeFragment);
            });
        }

        View btnNavMood = view.findViewById(R.id.btnNavMood);
        if (btnNavMood != null) {
            btnNavMood.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_moodSelectionFragment);
            });
        }

        View btnNavChat = view.findViewById(R.id.btnNavChat);
        if (btnNavChat != null) {
            btnNavChat.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_chatFragment);
            });
        }

        return view;
    }
}