package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import java.util.HashSet;
import java.util.Set;

public class HowCanWeHelpFragment extends Fragment {

    private Set<Integer> selectedSupportIds = new HashSet<>();
    private Button nextButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how_can_we_help, container, false);

        nextButton = view.findViewById(R.id.nextButtonHowHelp);
        
        int[] cardIds = {
            R.id.cardChatSupport, R.id.cardBreathing, 
            R.id.cardMeditation, R.id.cardJournaling, 
            R.id.cardAlerts
        };

        int[] selectIconIds = {
            R.id.selectChat, R.id.selectBreathing, 
            R.id.selectMeditation, R.id.selectJournaling, 
            R.id.selectAlerts
        };

        for (int i = 0; i < cardIds.length; i++) {
            final int index = i;
            CardView card = view.findViewById(cardIds[i]);
            ImageView selectIcon = view.findViewById(selectIconIds[i]);
            
            // Set initial state: gray circle
            selectIcon.setImageResource(0); // No icon initially
            
            card.setOnClickListener(v -> toggleSupportSelection(card, selectIcon, cardIds[index]));
        }

        nextButton.setOnClickListener(v -> {
            saveGoalsToBackend(view);
        });

        TextView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        return view;
    }

    private void saveGoalsToBackend(View view) {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        java.util.List<String> goals = new java.util.ArrayList<>();
        for (Integer id : selectedSupportIds) {
            if (id == R.id.cardChatSupport) goals.add("AI Support");
            if (id == R.id.cardBreathing) goals.add("Breathing");
            if (id == R.id.cardMeditation) goals.add("Meditation");
            if (id == R.id.cardJournaling) goals.add("Journaling");
            if (id == R.id.cardAlerts) goals.add("Smart Alerts");
        }

        java.util.Map<String, Object> profileUpdates = new java.util.HashMap<>();
        profileUpdates.put("goals", goals);

        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .updateUserProfile(userName, profileUpdates)
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    Navigation.findNavController(view).navigate(R.id.action_howCanWeHelpFragment_to_languageAccessibilityFragment);
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    Navigation.findNavController(view).navigate(R.id.action_howCanWeHelpFragment_to_languageAccessibilityFragment);
                }
            });
    }

    private void toggleSupportSelection(CardView card, ImageView selectIcon, int id) {
        if (selectedSupportIds.contains(id)) {
            selectedSupportIds.remove(id);
            // Deselected: Reset to empty gray circle
            selectIcon.setImageResource(0); 
            selectIcon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
        } else {
            selectedSupportIds.add(id);
            // Selected: Show tick mark and blue background
            selectIcon.setImageResource(R.drawable.ic_check);
            selectIcon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));
        }
        updateNextButton();
    }

    private void updateNextButton() {
        if (selectedSupportIds.size() > 0) {
            nextButton.setEnabled(true);
            nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));
            nextButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            nextButton.setEnabled(false);
            nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
            nextButton.setTextColor(getResources().getColor(R.color.desc_gray));
        }
    }
}