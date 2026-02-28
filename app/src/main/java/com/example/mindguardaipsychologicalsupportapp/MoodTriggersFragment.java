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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MoodTriggersFragment extends Fragment {

    private int moodImageResId;
    private String moodName;
    private int intensityValue;
    private String thoughts;
    private Set<Integer> selectedTriggerIds = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_triggers, container, false);

        // Get passed data
        if (getArguments() != null) {
            moodImageResId = getArguments().getInt("moodImage");
            moodName = getArguments().getString("moodName");
            intensityValue = getArguments().getInt("intensity");
            thoughts = getArguments().getString("thoughts");
        }

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
            Bundle bundle = new Bundle();
            bundle.putInt("moodImage", moodImageResId);
            bundle.putString("moodName", moodName);
            bundle.putInt("intensity", intensityValue);
            bundle.putString("thoughts", thoughts);

            ArrayList<String> triggerNames = new ArrayList<>();
            for (int id : selectedTriggerIds) {
                String t = mapTriggerIdToName(id);
                if (t != null) triggerNames.add(t);
            }
            bundle.putStringArrayList("triggers", triggerNames);
            
            Navigation.findNavController(view).navigate(R.id.action_moodTriggersFragment_to_reviewEntryFragment, bundle);
        });

        return view;
    }

    @Nullable
    private String mapTriggerIdToName(int id) {
        if (id == R.id.cardWork) return "Work";
        if (id == R.id.cardRelationships) return "Relationships";
        if (id == R.id.cardHealth) return "Health";
        if (id == R.id.cardFinance) return "Finance";
        if (id == R.id.cardFamily) return "Family";
        if (id == R.id.cardAcademic) return "Academic";
        if (id == R.id.cardSleep) return "Sleep";
        if (id == R.id.cardSocial) return "Social";
        if (id == R.id.cardOther) return "Other";
        return null;
    }
}