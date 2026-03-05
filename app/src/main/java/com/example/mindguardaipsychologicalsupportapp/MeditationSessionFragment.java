package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Locale;

public class MeditationSessionFragment extends Fragment {

    private TextView txtTimer, txtStatus;
    private FloatingActionButton btnPlay;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 300000; // Default 5 minutes
    private long initialTimeInMillis = 300000;
    
    private MaterialCardView tabCalm, tabFocus, tabSleep;
    private MaterialCardView chip5m, chip10m, chip15m, chip20m;
    private String currentMode = "Calm";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meditation_session, container, false);

        txtTimer = view.findViewById(R.id.txtTimer);
        txtStatus = view.findViewById(R.id.txtStatus);
        btnPlay = view.findViewById(R.id.btnPlay);

        tabCalm = view.findViewById(R.id.tabCalm);
        tabFocus = view.findViewById(R.id.tabFocus);
        tabSleep = view.findViewById(R.id.tabSleep);

        chip5m = view.findViewById(R.id.chip5m);
        chip10m = view.findViewById(R.id.chip10m);
        chip15m = view.findViewById(R.id.chip15m);
        chip20m = view.findViewById(R.id.chip20m);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        btnPlay.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        view.findViewById(R.id.btnReset).setOnClickListener(v -> resetTimer());

        // Tab click listeners
        tabCalm.setOnClickListener(v -> setMode("Calm"));
        tabFocus.setOnClickListener(v -> setMode("Focus"));
        tabSleep.setOnClickListener(v -> setMode("Sleep"));

        // Chip click listeners
        chip5m.setOnClickListener(v -> setDuration(5));
        chip10m.setOnClickListener(v -> setDuration(10));
        chip15m.setOnClickListener(v -> setDuration(15));
        chip20m.setOnClickListener(v -> setDuration(20));

        updateCountDownText();
        updateTabsUI();
        updateChipsUI();

        return view;
    }

    private void setMode(String mode) {
        if (isTimerRunning) pauseTimer();
        currentMode = mode;
        txtStatus.setText("Ready for " + mode);
        updateTabsUI();
    }

    private void setDuration(int minutes) {
        if (isTimerRunning) pauseTimer();
        initialTimeInMillis = minutes * 60 * 1000L;
        timeLeftInMillis = initialTimeInMillis;
        updateCountDownText();
        updateChipsUI();
    }

    private void updateTabsUI() {
        resetTabUI(tabCalm, R.id.imgCalm, R.id.txtCalm);
        resetTabUI(tabFocus, R.id.imgFocus, R.id.txtFocus);
        resetTabUI(tabSleep, R.id.imgSleep, R.id.txtSleep);

        if (currentMode.equals("Calm")) {
            setActiveTabUI(tabCalm, R.id.imgCalm, R.id.txtCalm);
        } else if (currentMode.equals("Focus")) {
            setActiveTabUI(tabFocus, R.id.imgFocus, R.id.txtFocus);
        } else if (currentMode.equals("Sleep")) {
            setActiveTabUI(tabSleep, R.id.imgSleep, R.id.txtSleep);
        }
    }

    private void resetTabUI(MaterialCardView tab, int imgId, int txtId) {
        tab.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
        ((ImageView) tab.findViewById(imgId)).setImageTintList(null);
        ((TextView) tab.findViewById(txtId)).setTextColor(ContextCompat.getColor(requireContext(), R.color.desc_gray));
    }

    private void setActiveTabUI(MaterialCardView tab, int imgId, int txtId) {
        tab.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_blue));
        ((ImageView) tab.findViewById(imgId)).setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
        ((TextView) tab.findViewById(txtId)).setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
    }

    private void updateChipsUI() {
        resetChipUI(chip5m, R.id.txt5m);
        resetChipUI(chip10m, R.id.txt10m);
        resetChipUI(chip15m, R.id.txt15m);
        resetChipUI(chip20m, R.id.txt20m);

        if (initialTimeInMillis == 5 * 60 * 1000L) setActiveChipUI(chip5m, R.id.txt5m);
        else if (initialTimeInMillis == 10 * 60 * 1000L) setActiveChipUI(chip10m, R.id.txt10m);
        else if (initialTimeInMillis == 15 * 60 * 1000L) setActiveChipUI(chip15m, R.id.txt15m);
        else if (initialTimeInMillis == 20 * 60 * 1000L) setActiveChipUI(chip20m, R.id.txt20m);
    }

    private void resetChipUI(MaterialCardView chip, int txtId) {
        chip.setCardBackgroundColor(ColorStateList.valueOf(android.graphics.Color.parseColor("#F0F4FF")));
        ((TextView) chip.findViewById(txtId)).setTextColor(ContextCompat.getColor(requireContext(), R.color.desc_gray));
    }

    private void setActiveChipUI(MaterialCardView chip, int txtId) {
        chip.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_blue));
        ((TextView) chip.findViewById(txtId)).setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
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
                btnPlay.setImageResource(android.R.drawable.ic_media_play);
                Bundle b = new Bundle();
                b.putString("exerciseName", currentMode + " Meditation");
                Navigation.findNavController(requireView()).navigate(R.id.action_meditationSessionFragment_to_toolCompleteFragment, b);
            }
        }.start();

        isTimerRunning = true;
        txtStatus.setText(currentMode + " session in progress...");
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        txtStatus.setText("Paused");
        btnPlay.setImageResource(android.R.drawable.ic_media_play);
    }

    private void resetTimer() {
        timeLeftInMillis = initialTimeInMillis;
        updateCountDownText();
        if (isTimerRunning) {
            pauseTimer();
        }
        txtStatus.setText("Ready for " + currentMode);
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
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
