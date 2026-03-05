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
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoodTriggersFragment extends Fragment {

    private int moodImageResId;
    private String moodName;
    private int intensityValue;
    private String thoughts;
    private Map<Integer, String> selectedTriggers = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_triggers, container, false);

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

        String[] triggerNames = {
                "Work", "Relationships", "Health", "Finance", "Family", "Academic", "Sleep", "Social", "Other"
        };

        for (int i = 0; i < cardIds.length; i++) {
            final int id = cardIds[i];
            final String name = triggerNames[i];
            MaterialCardView card = view.findViewById(id);
            if (card != null) {
                card.setOnClickListener(v -> {
                    if (selectedTriggers.containsKey(id)) {
                        selectedTriggers.remove(id);
                        card.setStrokeWidth(0);
                    } else {
                        selectedTriggers.put(id, name);
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
            bundle.putStringArrayList("triggers", new ArrayList<>(selectedTriggers.values()));
            
            Navigation.findNavController(view).navigate(R.id.action_moodTriggersFragment_to_reviewEntryFragment, bundle);
        });

        return view;
    }
}