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
            cardBreathing.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_toolBreathingFragment));
        }
        View cardFocusTimer = view.findViewById(R.id.cardFocusTimer);
        if (cardFocusTimer != null) {
            cardFocusTimer.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_toolFocusTimerFragment));
        }
        View cardSelfCare = view.findViewById(R.id.cardSelfCare);
        if (cardSelfCare != null) {
            cardSelfCare.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_toolSelfCareFragment));
        }

        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService api = com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
        api.getToolsDirectoryConfig().enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        java.util.Map<String, Object> body = response.body();
                        updateToolUI(view, body, "breathing", R.id.txtBadgeBreathing, R.id.txtDescBreathing);
                        updateToolUI(view, body, "meditation", R.id.txtBadgeMeditation, R.id.txtDescMeditation);
                        updateToolUI(view, body, "grounding", R.id.txtBadgeGrounding, R.id.txtDescGrounding);
                        updateToolUI(view, body, "focus", R.id.txtBadgeFocus, R.id.txtDescFocus);
                        updateToolUI(view, body, "selfcare", R.id.txtBadgeSelfCare, R.id.txtDescSelfCare);
                    } catch (Exception e) {}
                }
            }
            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {}
        });

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

        View btnNavProfile = view.findViewById(R.id.btnNavProfile);
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_toolsFragment_to_settingsFragment);
            });
        }

        return view;
    }

    private void updateToolUI(View view, java.util.Map<String, Object> body, String key, int badgeId, int descId) {
        if (body.containsKey(key)) {
            java.util.Map<String, Object> toolObj = (java.util.Map<String, Object>) body.get(key);
            if (toolObj != null) {
                if (toolObj.containsKey("badge")) {
                    android.widget.TextView badge = view.findViewById(badgeId);
                    if (badge != null) badge.setText((String) toolObj.get("badge"));
                }
                if (toolObj.containsKey("desc")) {
                    android.widget.TextView desc = view.findViewById(descId);
                    if (desc != null) desc.setText((String) toolObj.get("desc"));
                }
            }
        }
    }
}
