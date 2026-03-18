package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ToolMeditationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_meditation, container, false);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        }

        String[] recommendedMode = {"Calm"};
        int[] recommendedDuration = {5};

        android.widget.TextView titleMeditation = view.findViewById(R.id.titleMeditation);

        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService api = com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
        api.getMeditationContentConfig().enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        java.util.Map<String, Object> body = response.body();
                        if (body.containsKey("subtitle") && titleMeditation != null) {
                            titleMeditation.setText((String) body.get("subtitle"));
                        }
                        if (body.containsKey("recommended_mode")) recommendedMode[0] = (String) body.get("recommended_mode");
                        if (body.containsKey("recommended_duration")) recommendedDuration[0] = ((Number) body.get("recommended_duration")).intValue();
                    } catch (Exception e) {}
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {}
        });

        View btnOpenMeditation = view.findViewById(R.id.btnOpenMeditation);
        if (btnOpenMeditation != null) {
            btnOpenMeditation.setOnClickListener(v -> {
                Bundle b = new Bundle();
                b.putString("mode", recommendedMode[0]);
                b.putInt("duration", recommendedDuration[0]);
                Navigation.findNavController(view).navigate(R.id.action_toolMeditationFragment_to_meditationSessionFragment, b);
            });
        }

        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) btnNavHome.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolMeditationFragment_to_homeFragment));
        
        View btnNavMood = view.findViewById(R.id.btnNavMood);
        if (btnNavMood != null) btnNavMood.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolMeditationFragment_to_moodSelectionFragment));
        
        View btnNavChat = view.findViewById(R.id.btnNavChat);
        if (btnNavChat != null) btnNavChat.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolMeditationFragment_to_chatFragment));
        
        View btnNavTools = view.findViewById(R.id.btnNavTools);
        if (btnNavTools != null) btnNavTools.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolMeditationFragment_to_toolsFragment));
        
        View btnNavProfile = view.findViewById(R.id.btnNavProfile);
        if (btnNavProfile != null) btnNavProfile.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolMeditationFragment_to_settingsFragment));

        return view;
    }
}
