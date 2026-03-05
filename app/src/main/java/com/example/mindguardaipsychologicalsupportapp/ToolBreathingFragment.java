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

        view.findViewById(R.id.btnReset).setOnClickListener(v -> {
            resetBreathing();
        });

        view.findViewById(R.id.btnCompleteSession).setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("exerciseName", "Breathing");
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
            if (isRunning) {
                txtBreathLabel.setText("Hold");
                handler.postDelayed(() -> {
                    if (isRunning) {
                        txtBreathLabel.setText("Exhale");
                        handler.postDelayed(() -> {
                            if (isRunning) {
                                cycles++;
                                seconds += 19;
                                updateInfo();
                                startBreathingAnimation();
                            }
                        }, 8000);
                    }
                }, 7000);
            }
        }, 4000);
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
