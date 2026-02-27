package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.card.MaterialCardView;
import java.util.HashSet;
import java.util.Set;

public class MoodTriggersFragment extends Fragment {

    private Set<Integer> selectedTriggerIds = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_triggers, container, false);

        int[] cardIds = {
                R.id.cardWork, R.id.cardRelationships, R.id.cardHealth,
                R.id.cardFinance, R.id.cardFamily, R.id.cardAcademic,
                R.id.cardSleep, R.id.cardSocial, R.id.cardOther
        };

        for (int id : cardIds) {
            MaterialCardView card = view.findViewById(id);
            if (card != null) {
                card.setOnClickListener(v -> {
                    if (selectedTriggerIds.contains(id)) {
                        selectedTriggerIds.remove(id);
                        card.setStrokeWidth(0);
                    } else {
                        selectedTriggerIds.add(id);
                        card.setStrokeColor(getResources().getColor(R.color.button_blue));
                        card.setStrokeWidth(4);
                    }
                });
            }
        }

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        view.findViewById(R.id.btnReviewEntry).setOnClickListener(v -> {
            // Finish flow and go to home
            Navigation.findNavController(view).navigate(R.id.action_moodTriggersFragment_to_homeFragment);
        });

        return view;
    }
}