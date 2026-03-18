package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.card.MaterialCardView;

public class ToolBreathingFragment extends Fragment {

    private boolean isRunning = false;
    private int seconds = 0;
    private int cycles = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private TextView txtBreathLabel, txtSessionInfo;
    private MaterialCardView btnPlay;

    private int inhaleMs = 4000;
    private int holdMs = 7000;
    private int exhaleMs = 8000;
    private int waitMs = 0;
    private String patternName = "Breathing";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_breathing, container, false);

        txtBreathLabel = view.findViewById(R.id.breathLabel);
        txtSessionInfo = view.findViewById(R.id.sessionInfo);
        btnPlay = view.findViewById(R.id.btnPlay);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        btnPlay.setOnClickListener(v -> {
            if (isRunning) {
                pauseBreathing();
            } else {
                startBreathing();
            }
        });

        // Fetch Backend Config
        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService api = com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
        api.getBreathingPattern().enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        java.util.Map<String, Object> body = response.body();
                        if (body.containsKey("inhale_ms")) inhaleMs = ((Number) body.get("inhale_ms")).intValue();
                        if (body.containsKey("hold_ms")) holdMs = ((Number) body.get("hold_ms")).intValue();
                        if (body.containsKey("exhale_ms")) exhaleMs = ((Number) body.get("exhale_ms")).intValue();
                        if (body.containsKey("wait_ms")) waitMs = ((Number) body.get("wait_ms")).intValue();
                        if (body.containsKey("name")) patternName = (String) body.get("name");
                    } catch (Exception e) {}
                }
            }
            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {}
        });

        view.findViewById(R.id.btnReset).setOnClickListener(v -> {
            resetBreathing();
        });

        view.findViewById(R.id.btnCompleteSession).setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("exerciseName", patternName);
            b.putInt("duration", Math.max(1, seconds / 60));
            Navigation.findNavController(view).navigate(R.id.action_toolBreathingFragment_to_toolCompleteFragment, b);
        });

        // Bottom Navigation
        view.findViewById(R.id.btnNavHome).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolBreathingFragment_to_homeFragment));
        view.findViewById(R.id.btnNavMood).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolBreathingFragment_to_moodSelectionFragment));
        view.findViewById(R.id.btnNavChat).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolBreathingFragment_to_chatFragment));
        view.findViewById(R.id.btnNavTools).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolBreathingFragment_to_toolsFragment));
        view.findViewById(R.id.btnNavProfile).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolBreathingFragment_to_settingsFragment));

        return view;
    }

    private void startBreathing() {
        isRunning = true;
        updatePlayIcon("▮▮");
        startBreathingAnimation();
    }

    private void pauseBreathing() {
        isRunning = false;
        updatePlayIcon("▶");
        handler.removeCallbacksAndMessages(null);
        txtBreathLabel.setText("Paused");
    }

    private void resetBreathing() {
        pauseBreathing();
        seconds = 0;
        cycles = 0;
        txtBreathLabel.setText("Ready");
        updateInfo();
    }

    private void updatePlayIcon(String icon) {
        if (btnPlay.getChildAt(0) instanceof TextView) {
            ((TextView) btnPlay.getChildAt(0)).setText(icon);
        } else if (btnPlay.getChildAt(0) instanceof LinearLayout) {
             LinearLayout layout = (LinearLayout) btnPlay.getChildAt(0);
             for (int i = 0; i < layout.getChildCount(); i++) {
                 if (layout.getChildAt(i) instanceof TextView) {
                     ((TextView) layout.getChildAt(i)).setText(icon);
                     break;
                 }
             }
        }
    }

    private void startBreathingAnimation() {
        if (!isRunning) return;
        
        txtBreathLabel.setText("Inhale");
        handler.postDelayed(() -> {
            if (!isRunning) return;
            txtBreathLabel.setText("Hold");
            handler.postDelayed(() -> {
                if (!isRunning) return;
                txtBreathLabel.setText("Exhale");
                handler.postDelayed(() -> {
                    if (!isRunning) return;
                    
                    Runnable cycleEnd = () -> {
                        cycles++;
                        seconds += (inhaleMs + holdMs + exhaleMs + waitMs) / 1000;
                        updateInfo();
                        startBreathingAnimation();
                    };

                    if (waitMs > 0) {
                        txtBreathLabel.setText("Wait");
                        handler.postDelayed(cycleEnd, waitMs);
                    } else {
                        cycleEnd.run();
                    }
                    
                }, exhaleMs);
            }, holdMs);
        }, inhaleMs);
    }

    private void updateInfo() {
        int mins = seconds / 60;
        int secs = seconds % 60;
        txtSessionInfo.setText(String.format("Session: %d cycles · %02d:%02d", cycles, mins, secs));
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseBreathing();
    }
}
