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

            if (imgMoodReview != null) imgMoodReview.setImageResource(moodImage);
            if (txtMoodNameReview != null) txtMoodNameReview.setText(moodName);
            if (progressIntensity != null) progressIntensity.setProgress(intensity);
            if (txtIntensity != null) txtIntensity.setText(intensity + " % intensity");

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
            // Disable button while saving to prevent double clicks
            v.setEnabled(false);

            // Save the entry before navigating
            MoodEntry entry = MoodEntry.createNow(moodName, moodImage, intensity, triggers, "");
            
            MoodEntryStorage.add(requireContext(), entry, new MoodEntryStorage.MoodAddCallback() {
                @Override
                public void onSuccess(MoodEntry savedEntry) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        // Corrected: Navigate to Mood History on success using the action ID from nav_graph
                        Navigation.findNavController(view).navigate(R.id.action_reviewEntryFragment_to_moodHistoryFragment);
                    });
                }

                @Override
                public void onError(String message) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        v.setEnabled(true);
                        android.widget.Toast.makeText(requireContext(), "Failed to save: " + message, android.widget.Toast.LENGTH_LONG).show();
                    });
                }
            });
        });

        // Bottom Navigation Listeners
        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.homeFragment));
        }

        View btnNavMood = view.findViewById(R.id.btnNavMood);
        if (btnNavMood != null) {
            btnNavMood.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.moodSelectionFragment));
        }

        View btnNavChat = view.findViewById(R.id.btnNavChat);
        if (btnNavChat != null) {
            btnNavChat.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.chatFragment);
            });
        }

        View btnNavTools = view.findViewById(R.id.btnNavTools);
        if (btnNavTools != null) {
            btnNavTools.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.toolsFragment);
            });
        }

        View btnNavProfile = view.findViewById(R.id.btnNavProfile);
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.settingsFragment);
            });
        }

        return view;
    }
}