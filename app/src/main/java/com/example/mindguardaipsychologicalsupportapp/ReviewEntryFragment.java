package com.example.mindguardaipsychologicalsupportapp;

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
import java.util.List;

public class ReviewEntryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_entry, container, false);

        ImageView imgMoodReview = view.findViewById(R.id.imgMoodReview);
        TextView txtMoodNameReview = view.findViewById(R.id.txtMoodNameReview);
        LinearProgressIndicator progressIntensity = view.findViewById(R.id.progressIntensityReview);
        TextView txtIntensity = view.findViewById(R.id.txtIntensityReview);
        ChipGroup chipGroup = view.findViewById(R.id.chipGroupTriggersReview);

        int moodImage = R.drawable.img_28;
        int intensity = 50;
        String moodName = "Great";
        String thoughts = "";
        ArrayList<String> triggers = new ArrayList<>();

        // Get data from bundle
        if (getArguments() != null) {
            moodImage = getArguments().getInt("moodImage", R.drawable.img_28);
            intensity = getArguments().getInt("intensity", 50);
            moodName = getArguments().getString("moodName", "Great");
            thoughts = getArguments().getString("thoughts", "");
            ArrayList<String> t = getArguments().getStringArrayList("triggers");
            if (t != null) triggers = t;

            imgMoodReview.setImageResource(moodImage);
            txtMoodNameReview.setText(moodName);
            progressIntensity.setProgress(intensity);
            txtIntensity.setText(intensity + " % intensity");
        }

        if (chipGroup != null) {
            chipGroup.removeAllViews();
            if (triggers.isEmpty()) {
                chipGroup.setVisibility(View.GONE);
            } else {
                chipGroup.setVisibility(View.VISIBLE);
                for (String tr : triggers) {
                    Chip c = new Chip(requireContext());
                    c.setText(tr);
                    c.setClickable(false);
                    c.setCheckable(false);
                    chipGroup.addView(c);
                }
            }
        }

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        final int finalMoodImage = moodImage;
        final int finalIntensity = intensity;
        final String finalMoodName = moodName;
        final String finalThoughts = thoughts;
        final List<String> finalTriggers = triggers;

        view.findViewById(R.id.btnSaveEntry).setOnClickListener(v -> {
            MoodEntry entry = MoodEntry.createNow(finalMoodName, finalMoodImage, finalIntensity, finalTriggers, finalThoughts);
            MoodEntryStorage.add(requireContext(), entry);
            Navigation.findNavController(view).navigate(R.id.action_reviewEntryFragment_to_moodHistoryFragment);
        });

        return view;
    }
}