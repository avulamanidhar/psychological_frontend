package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileOverviewFragment extends Fragment {

    private TextView profileName;
    private ImageView profileAvatar;
    private ChipGroup goalsChipGroup, supportChipGroup;
    private MindGuardApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_overview, container, false);

        apiService = RetrofitClient.getApiService();

        profileName = view.findViewById(R.id.profileName);
        profileAvatar = view.findViewById(R.id.profileAvatarImage);
        goalsChipGroup = view.findViewById(R.id.goalsChipGroup);
        supportChipGroup = view.findViewById(R.id.supportChipGroup);

        Button startJourneyButton = view.findViewById(R.id.startJourneyButton);
        startJourneyButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_profileOverviewFragment_to_homeFragment));

        TextView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        loadProfileData();

        return view;
    }

    private void loadProfileData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");
        
        apiService.getUserProfile(userName).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    profileName.setText(userName);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                profileName.setText(userName);
            }
        });
    }

    private void updateUI(Map<String, Object> profile) {
        String name = (String) profile.get("username");
        profileName.setText(name != null ? name : "User");

        String avatar = (String) profile.get("avatar_name");
        if (avatar != null) {
            int resId = getResources().getIdentifier(avatar, "drawable", requireContext().getPackageName());
            if (resId != 0) {
                profileAvatar.setImageResource(resId);
            }
        }

        List<String> goals = (List<String>) profile.get("goals");
        if (goals != null) {
            goalsChipGroup.removeAllViews();
            for (String goal : goals) {
                Chip chip = new Chip(getContext());
                chip.setText(goal);
                chip.setChipBackgroundColorResource(R.color.bg_splash);
                chip.setTextColor(getResources().getColor(R.color.button_blue));
                goalsChipGroup.addView(chip);
            }
        }
        
        // Support types - we use some mock or derived data for now
        String[] supports = {"24/7 AI Chat", "Private Journal", "Stress Analysis"};
        supportChipGroup.removeAllViews();
        for (String s : supports) {
            Chip chip = new Chip(getContext());
            chip.setText(s);
            chip.setCheckable(false);
            supportChipGroup.addView(chip);
        }
    }
}