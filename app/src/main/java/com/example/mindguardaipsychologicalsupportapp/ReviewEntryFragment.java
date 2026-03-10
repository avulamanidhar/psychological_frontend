package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        view.findViewById(R.id.btnSaveEntry).setOnClickListener(v -> {
            v.setEnabled(false); // Disable to prevent double save

            // Create entry
            MoodEntry entry = MoodEntry.createNow(moodName, moodImage, intensity, triggers, "");
            
            // Fixed: Provide a fallback navigation if API fails
            MoodEntryStorage.add(requireContext(), entry, new MoodEntryStorage.MoodAddCallback() {
                @Override
                public void onSuccess(MoodEntry savedEntry) {
                    navigateToHistory(view);
                }

                @Override
                public void onError(String message) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Saved locally (API unreachable)", Toast.LENGTH_SHORT).show();
                        // Navigate anyway so the user isn't stuck
                        navigateToHistory(view);
                    });
                }
            });
        });

        // Setup Bottom Navigation
        setupBottomNavigation(view);

        return view;
    }

    private void navigateToHistory(View view) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            try {
                Navigation.findNavController(view).navigate(R.id.action_reviewEntryFragment_to_moodHistoryFragment);
            } catch (Exception e) {
                // Fallback to direct fragment ID if action fails
                Navigation.findNavController(view).navigate(R.id.moodHistoryFragment);
            }
        });
    }

    private void setupBottomNavigation(View view) {
        view.findViewById(R.id.btnNavHome).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.homeFragment));
        view.findViewById(R.id.btnNavMood).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.moodSelectionFragment));
        view.findViewById(R.id.btnNavChat).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.chatFragment));
        view.findViewById(R.id.btnNavTools).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.toolsFragment));
        view.findViewById(R.id.btnNavProfile).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.settingsFragment));
    }
}