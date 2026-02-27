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
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ReviewEntryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_entry, container, false);

        ImageView imgMoodReview = view.findViewById(R.id.imgMoodReview);
        TextView txtMoodNameReview = view.findViewById(R.id.txtMoodNameReview);
        LinearProgressIndicator progressIntensity = view.findViewById(R.id.progressIntensityReview);
        TextView txtIntensity = view.findViewById(R.id.txtIntensityReview);

        // Get data from bundle
        if (getArguments() != null) {
            int moodImage = getArguments().getInt("moodImage", R.drawable.img_28);
            int intensity = getArguments().getInt("intensity", 50);
            String moodName = getArguments().getString("moodName", "Great");

            imgMoodReview.setImageResource(moodImage);
            txtMoodNameReview.setText(moodName);
            progressIntensity.setProgress(intensity);
            txtIntensity.setText(intensity + " % intensity");
        }

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        view.findViewById(R.id.btnSaveEntry).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_reviewEntryFragment_to_homeFragment);
        });

        return view;
    }
}