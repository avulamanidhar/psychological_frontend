package com.example.mindguardaipsychologicalsupportapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ToolGroundingFragment extends Fragment {

    private static String[] PHRASES = {
            "things you can see",
            "things you can touch",
            "things you can hear",
            "things you can smell",
            "things you can taste"
    };
    private static String[] INSTRUCTIONS = {
            "Name 5 things you can see around you right now",
            "Name 4 things you can physically feel or touch",
            "Name 3 sounds you can hear in your environment",
            "Name 2 things you can smell (or like to smell)",
            "Name 1 thing you can taste right now"
    };
    private static int[] NUMBERS = { 5, 4, 3, 2, 1 };
    private static int DURATION_MINUTES = 5;
    private static final int[] BAR_IDS = { R.id.bar1, R.id.bar2, R.id.bar3, R.id.bar4, R.id.bar5 };
    private static final int[] ICON_IDS = { R.drawable.img_70, R.drawable.img_71, R.drawable.img_72, R.drawable.img_73, R.drawable.img_74 };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_grounding, container, false);
        int step = 1;
        if (getArguments() != null) {
            step = getArguments().getInt("step", 1);
        }
        if (step < 1 || step > 5) step = 1;

        final int currentStep = step;

        TextView stepNumber = view.findViewById(R.id.stepNumber);
        TextView stepPhrase = view.findViewById(R.id.stepPhrase);
        TextView stepInstruction = view.findViewById(R.id.stepInstruction);
        TextView stepCounter = view.findViewById(R.id.stepCounter);
        ImageView stepIcon = view.findViewById(R.id.stepIcon);

        // Fetch Config on First Step
        if (currentStep == 1) {
            com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService api = com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
            api.getGroundingExerciseConfig().enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            java.util.Map<String, Object> body = response.body();
                            if (body.containsKey("duration_minutes")) {
                                DURATION_MINUTES = ((Number) body.get("duration_minutes")).intValue();
                            }
                            if (body.containsKey("steps")) {
                                java.util.List<java.util.Map<String, Object>> stepsList = (java.util.List<java.util.Map<String, Object>>) body.get("steps");
                                if (stepsList != null && stepsList.size() == 5) {
                                    for (int i = 0; i < 5; i++) {
                                        java.util.Map<String, Object> stepData = stepsList.get(i);
                                        if (stepData.containsKey("number")) NUMBERS[i] = ((Number) stepData.get("number")).intValue();
                                        if (stepData.containsKey("phrase")) PHRASES[i] = (String) stepData.get("phrase");
                                        if (stepData.containsKey("instruction")) INSTRUCTIONS[i] = (String) stepData.get("instruction");
                                    }
                                    // Update UI since it's step 1
                                    if (stepNumber != null) stepNumber.setText(String.valueOf(NUMBERS[0]));
                                    if (stepPhrase != null) stepPhrase.setText(PHRASES[0]);
                                    if (stepInstruction != null) stepInstruction.setText(INSTRUCTIONS[0]);
                                }
                            }
                        } catch (Exception e) {}
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {}
            });
        }

        // Progress bars
        for (int i = 0; i < 5; i++) {
            View bar = view.findViewById(BAR_IDS[i]);
            if (bar != null) {
                bar.setBackgroundColor(i < currentStep ? getResources().getColor(R.color.button_blue) : Color.parseColor("#E0E0E0"));
            }
        }

        Button btnPrevious = view.findViewById(R.id.btnPrevious);
        Button btnNext = view.findViewById(R.id.btnNext);

        if (stepNumber != null) stepNumber.setText(String.valueOf(NUMBERS[currentStep - 1]));
        if (stepPhrase != null) stepPhrase.setText(PHRASES[currentStep - 1]);
        if (stepInstruction != null) stepInstruction.setText(INSTRUCTIONS[currentStep - 1]);
        if (stepCounter != null) stepCounter.setText("Step " + currentStep + " of 5");
        if (stepIcon != null) stepIcon.setImageResource(ICON_IDS[currentStep - 1]);

        if (currentStep == 5) {
            if (btnNext != null) {
                btnNext.setText("Complete ✓");
            }
        }

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        if (btnPrevious != null) {
            btnPrevious.setOnClickListener(v -> {
                if (currentStep <= 1) {
                    Navigation.findNavController(view).navigate(R.id.action_toolGroundingFragment_to_toolsFragment);
                } else {
                    Navigation.findNavController(view).navigateUp();
                }
            });
        }
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                if (currentStep >= 5) {
                    Bundle b = new Bundle();
                    b.putString("exerciseName", "Grounding");
                    b.putInt("duration", Math.max(1, DURATION_MINUTES));
                    Navigation.findNavController(view).navigate(R.id.action_toolGroundingFragment_to_toolCompleteFragment, b);
                } else {
                    Bundle b = new Bundle();
                    b.putInt("step", currentStep + 1);
                    Navigation.findNavController(view).navigate(R.id.toolGroundingFragment, b);
                }
            });
        }

        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) btnNavHome.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolGroundingFragment_to_homeFragment));
        View btnNavMood = view.findViewById(R.id.btnNavMood);
        if (btnNavMood != null) btnNavMood.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolGroundingFragment_to_moodSelectionFragment));
        View btnNavChat = view.findViewById(R.id.btnNavChat);
        if (btnNavChat != null) btnNavChat.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolGroundingFragment_to_chatFragment));
        View btnNavTools = view.findViewById(R.id.btnNavTools);
        if (btnNavTools != null) btnNavTools.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolGroundingFragment_to_toolsFragment));
        View btnNavProfile = view.findViewById(R.id.btnNavProfile);
        if (btnNavProfile != null) btnNavProfile.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolGroundingFragment_to_settingsFragment));

        return view;
    }
}