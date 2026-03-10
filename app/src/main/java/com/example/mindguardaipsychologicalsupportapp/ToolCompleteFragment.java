package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToolCompleteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_complete, container, false);

        String exerciseName = "exercise";
        if (getArguments() != null && getArguments().containsKey("exerciseName")) {
            exerciseName = getArguments().getString("exerciseName", "exercise");
        }
        TextView subtitleComplete = view.findViewById(R.id.subtitleComplete);
        if (subtitleComplete != null) {
            subtitleComplete.setText("You completed the " + exerciseName + " exercise. How do you feel now?");
        }

        logActivityToBackend(exerciseName);

        view.findViewById(R.id.btnBackToTools).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_toolsFragment);
        });

        View cardBetter = view.findViewById(R.id.cardBetter);
        View cardSame = view.findViewById(R.id.cardSame);
        View cardStillTough = view.findViewById(R.id.cardStillTough);
        if (cardBetter != null) cardBetter.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_toolsFragment));
        if (cardSame != null) cardSame.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_toolsFragment));
        if (cardStillTough != null) cardStillTough.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_toolsFragment));

        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) btnNavHome.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_homeFragment));
        View btnNavMood = view.findViewById(R.id.btnNavMood);
        if (btnNavMood != null) btnNavMood.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_moodSelectionFragment));
        View btnNavChat = view.findViewById(R.id.btnNavChat);
        if (btnNavChat != null) btnNavChat.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_chatFragment));
        View btnNavTools = view.findViewById(R.id.btnNavTools);
        if (btnNavTools != null) btnNavTools.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_toolsFragment));
        View btnNavProfile = view.findViewById(R.id.btnNavProfile);
        if (btnNavProfile != null) btnNavProfile.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_settingsFragment));

        return view;
    }

    private void logActivityToBackend(String exerciseName) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        MindGuardApiService api = RetrofitClient.getApiService();
        
        // Fetch user profile to get ID
        api.getUserProfile(userName).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Double userIdDouble = (Double) response.body().get("id");
                    if (userIdDouble != null) {
                        int userId = userIdDouble.intValue();
                        Map<String, Object> payload = new HashMap<>();
                        payload.put("user", userId);
                        payload.put("activity_type", exerciseName);
                        payload.put("duration_minutes", 10);

                        api.logActivity(payload).enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {}
                            @Override
                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
        });
    }
}
