package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.card.MaterialCardView;
import java.util.Locale;

public class ToolFocusTimerFragment extends Fragment {

    private TextView txtTimer, txtTimerLabel;
    private MaterialCardView btnPlay, tabFocus, tabBreak;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 1500000; // 25 minutes
    private long initialTimeInMillis = 1500000;
    private boolean isFocusMode = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_focus_timer, container, false);

        txtTimer = view.findViewById(R.id.timerText);
        txtTimerLabel = view.findViewById(R.id.timerLabel);
        btnPlay = view.findViewById(R.id.btnPlay);
        tabFocus = view.findViewById(R.id.tabFocus);
        tabBreak = view.findViewById(R.id.tabBreak);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        btnPlay.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        view.findViewById(R.id.btnReset).setOnClickListener(v -> resetTimer());

        tabFocus.setOnClickListener(v -> setMode(true));
        tabBreak.setOnClickListener(v -> setMode(false));

        // Bottom Navigation
        view.findViewById(R.id.btnNavHome).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolFocusTimerFragment_to_homeFragment));
        view.findViewById(R.id.btnNavMood).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolFocusTimerFragment_to_moodSelectionFragment));
        view.findViewById(R.id.btnNavChat).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolFocusTimerFragment_to_chatFragment));
        view.findViewById(R.id.btnNavTools).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolFocusTimerFragment_to_toolsFragment));
        view.findViewById(R.id.btnNavProfile).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolFocusTimerFragment_to_settingsFragment));

        updateCountDownText();
        updateTabsUI();

        return view;
    }

    private void setMode(boolean focus) {
        if (isTimerRunning) pauseTimer();
        isFocusMode = focus;
        initialTimeInMillis = isFocusMode ? 1500000 : 300000; // 25m or 5m
        timeLeftInMillis = initialTimeInMillis;
        txtTimerLabel.setText(isFocusMode ? "Focus Time" : "Break Time");
        updateCountDownText();
        updateTabsUI();
    }

    private void updateTabsUI() {
        if (isFocusMode) {
            tabFocus.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_blue));
            ((TextView) ((LinearLayout) tabFocus.getChildAt(0)).getChildAt(1)).setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            
            tabBreak.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
            ((TextView) ((LinearLayout) tabBreak.getChildAt(0)).getChildAt(1)).setTextColor(ContextCompat.getColor(requireContext(), R.color.desc_gray));
        } else {
            tabBreak.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_blue));
            ((TextView) ((LinearLayout) tabBreak.getChildAt(0)).getChildAt(1)).setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            
            tabFocus.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
            ((TextView) ((LinearLayout) tabFocus.getChildAt(0)).getChildAt(1)).setTextColor(ContextCompat.getColor(requireContext(), R.color.desc_gray));
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                updatePlayIcon("▶");
                Bundle b = new Bundle();
                b.putString("exerciseName", isFocusMode ? "Focus Session" : "Break Session");
                Navigation.findNavController(requireView()).navigate(R.id.action_toolFocusTimerFragment_to_toolCompleteFragment, b);
            }
        }.start();

        isTimerRunning = true;
        updatePlayIcon("▮▮");
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        updatePlayIcon("▶");
    }

    private void resetTimer() {
        timeLeftInMillis = initialTimeInMillis;
        updateCountDownText();
        if (isTimerRunning) {
            pauseTimer();
        }
    }

    private void updatePlayIcon(String icon) {
        if (btnPlay.getChildAt(0) instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) btnPlay.getChildAt(0);
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setText(icon);
                    break;
                }
            }
        } else if (btnPlay.getChildAt(0) instanceof TextView) {
             ((TextView) btnPlay.getChildAt(0)).setText(icon);
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        txtTimer.setText(timeLeftFormatted);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
