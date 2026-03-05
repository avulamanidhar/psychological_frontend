package com.example.mindguardaipsychologicalsupportapp;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class MentalHealthScoreFragment extends Fragment {

    private TextView txtMainScore, txtMoodScore, txtStressScore, txtSleepScore, txtSocialScore;
    private ProgressBar circularProgressBar, progressMood, progressStress, progressSleep, progressSocial;
    private View txtScoreLabel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mental_health_score, container, false);

        txtMainScore = view.findViewById(R.id.txtMainScore);
        txtScoreLabel = view.findViewById(R.id.txtScoreLabel);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        
        txtMoodScore = view.findViewById(R.id.txtMoodScore);
        progressMood = view.findViewById(R.id.progressMood);
        
        txtStressScore = view.findViewById(R.id.txtStressScore);
        progressStress = view.findViewById(R.id.progressStress);

        txtSleepScore = view.findViewById(R.id.txtSleepScore);
        progressSleep = view.findViewById(R.id.progressSleep);

        txtSocialScore = view.findViewById(R.id.txtSocialScore);
        progressSocial = view.findViewById(R.id.progressSocial);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        animateScores();

        return view;
    }

    private void animateScores() {
        // Main Score Animation: 0 to 78
        animateValue(0, 78, 1500, (value) -> {
            txtMainScore.setText(String.valueOf(value));
            circularProgressBar.setProgress(value);
            if (value == 78) {
                txtScoreLabel.setVisibility(View.VISIBLE);
            }
        });

        // Mood Score Animation: 0 to 82
        animateValue(0, 82, 1200, (value) -> {
            txtMoodScore.setText(value + " /100");
            progressMood.setProgress(value);
        });

        // Stress Score Animation: 0 to 65
        animateValue(0, 65, 1000, (value) -> {
            txtStressScore.setText(value + " /100");
            progressStress.setProgress(value);
        });

        // Sleep Score Animation: 0 to 71
        animateValue(0, 71, 1100, (value) -> {
            txtSleepScore.setText(value + " /100");
            progressSleep.setProgress(value);
        });

        // Social Score Animation: 0 to 88
        animateValue(0, 88, 1300, (value) -> {
            txtSocialScore.setText(value + " /100");
            progressSocial.setProgress(value);
        });
    }

    private void animateValue(int start, int end, long duration, ValueUpdateListener listener) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            listener.onUpdate((int) animation.getAnimatedValue());
        });
        animator.start();
    }

    interface ValueUpdateListener {
        void onUpdate(int value);
    }
}
