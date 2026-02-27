package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.google.android.material.slider.Slider;

public class MoodIntensityFragment extends Fragment {

    private int moodImageResId = R.drawable.img_28;
    private String moodName = "Great";
    private int intensityValue = 14;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_intensity, container, false);

        ImageView imgMoodEmoji = view.findViewById(R.id.imgMoodEmoji);
        Slider slider = view.findViewById(R.id.sliderIntensity);
        TextView txtPercent = view.findViewById(R.id.txtIntensityPercent);
        TextView txtDesc = view.findViewById(R.id.txtIntensityDesc);

        // Get passed mood data
        if (getArguments() != null) {
            moodImageResId = getArguments().getInt("moodImage", R.drawable.img_28);
            moodName = getArguments().getString("moodName", "Great");
            imgMoodEmoji.setImageResource(moodImageResId);
        }

        slider.addOnChangeListener((s, value, fromUser) -> {
            intensityValue = (int) value;
            txtPercent.setText(intensityValue + "%");
            
            if (intensityValue < 30) {
                txtDesc.setText("Feeling quite calm and centered");
                slider.setTrackActiveTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                slider.setThumbStrokeColor(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            } else if (intensityValue < 70) {
                txtDesc.setText("Manageable");
                slider.setTrackActiveTintList(ColorStateList.valueOf(Color.parseColor("#4A90E2")));
                slider.setThumbStrokeColor(ColorStateList.valueOf(Color.parseColor("#4A90E2")));
            } else {
                txtDesc.setText("Let's work through this together");
                slider.setTrackActiveTintList(ColorStateList.valueOf(Color.parseColor("#E91E63")));
                slider.setThumbStrokeColor(ColorStateList.valueOf(Color.parseColor("#E91E63")));
            }
        });

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("moodImage", moodImageResId);
            bundle.putString("moodName", moodName);
            bundle.putInt("intensity", intensityValue);
            Navigation.findNavController(view).navigate(R.id.action_moodIntensityFragment_to_moodThoughtsFragment, bundle);
        });

        // Bottom Navigation
        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> 
                Navigation.findNavController(view).navigate(R.id.homeFragment));
        }

        return view;
    }
}