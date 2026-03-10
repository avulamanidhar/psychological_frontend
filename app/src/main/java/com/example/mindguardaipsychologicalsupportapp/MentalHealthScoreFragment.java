package com.example.mindguardaipsychologicalsupportapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        loadScores();

        return view;
    }

    private void loadScores() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        MindGuardApiService api = RetrofitClient.getApiService();
        api.getHealthScoreDetail(userName).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    
                    int main = ((Double) data.get("main_score")).intValue();
                    int mood = ((Double) data.get("mood_score")).intValue();
                    int stress = ((Double) data.get("stress_score")).intValue();
                    int sleep = ((Double) data.get("sleep_score")).intValue();
                    int social = ((Double) data.get("social_score")).intValue();
                    
                    animateScores(main, mood, stress, sleep, social);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load scores", Toast.LENGTH_SHORT).show();
                animateScores(78, 82, 65, 71, 88); // fallback to defaults
            }
        });
    }

    private void animateScores(int main, int mood, int stress, int sleep, int social) {
        // Main Score Animation
        animateValue(0, main, 1500, (value) -> {
            txtMainScore.setText(String.valueOf(value));
            circularProgressBar.setProgress(value);
            if (value == main) {
                txtScoreLabel.setVisibility(View.VISIBLE);
            }
        });

        // Mood Score Animation
        animateValue(0, mood, 1200, (value) -> {
            txtMoodScore.setText(value + " /100");
            progressMood.setProgress(value);
        });

        // Stress Score Animation
        animateValue(0, stress, 1000, (value) -> {
            txtStressScore.setText(value + " /100");
            progressStress.setProgress(value);
        });

        // Sleep Score Animation
        animateValue(0, sleep, 1100, (value) -> {
            txtSleepScore.setText(value + " /100");
            progressSleep.setProgress(value);
        });

        // Social Score Animation
        animateValue(0, social, 1300, (value) -> {
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
