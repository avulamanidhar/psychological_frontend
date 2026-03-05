package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.ArrayList;

public class ReviewEntryFragment extends Fragment {

    private int moodImage;
    private int intensity;
    private String moodName;
    private ArrayList<String> triggers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_entry, container, false);

        ImageView imgMoodReview = view.findViewById(R.id.imgMoodReview);
        TextView txtMoodNameReview = view.findViewById(R.id.txtMoodNameReview);
        LinearProgressIndicator progressIntensity = view.findViewById(R.id.progressIntensityReview);
        TextView txtIntensity = view.findViewById(R.id.txtIntensityReview);
        ChipGroup chipGroupTriggers = view.findViewById(R.id.chipGroupTriggersReview);

        // Get data from bundle
        if (getArguments() != null) {
            moodImage = getArguments().getInt("moodImage", R.drawable.img_28);
            intensity = getArguments().getInt("intensity", 50);
            moodName = getArguments().getString("moodName", "Great");
            triggers = getArguments().getStringArrayList("triggers");

            imgMoodReview.setImageResource(moodImage);
            txtMoodNameReview.setText(moodName);
            progressIntensity.setProgress(intensity);
            txtIntensity.setText(intensity + " % intensity");

            // Display dynamic triggers
            if (chipGroupTriggers != null && triggers != null) {
                chipGroupTriggers.removeAllViews();
                for (String trigger : triggers) {
                    Chip chip = new Chip(requireContext());
                    chip.setText(trigger);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.icon_bg_blue)));
                    chip.setTextColor(getResources().getColor(R.color.button_blue));
                    chip.setChipCornerRadius(24f);
                    chip.setChipStrokeWidth(0f);
                    chipGroupTriggers.addView(chip);
                }
            }
        }

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        view.findViewById(R.id.btnSaveEntry).setOnClickListener(v -> {
            // Save the entry before navigating
            MoodEntry entry = MoodEntry.createNow(moodName, moodImage, intensity, triggers, "");
            MoodEntryStorage.add(requireContext(), entry);
            
            // Navigate to Mood History
            Navigation.findNavController(view).navigate(R.id.action_reviewEntryFragment_to_moodHistoryFragment);
        });

        return view;
    }
}
