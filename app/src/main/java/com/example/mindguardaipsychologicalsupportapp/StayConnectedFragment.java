package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class StayConnectedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stay_connected, container, false);

        // Set up Notification Time Dropdown
        android.widget.AutoCompleteTextView timeAutoComplete = view.findViewById(R.id.timeAutoComplete);
        if (timeAutoComplete != null) {
            String[] timeOptions = new String[]{
                "06:00 AM", "06:30 AM", "07:00 AM", "07:30 AM", 
                "08:00 AM", "08:30 AM", "09:00 AM", "09:30 AM", 
                "10:00 AM", "10:30 AM", "11:00 AM"
            };
            android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    timeOptions
            );
            timeAutoComplete.setAdapter(adapter);
        }

        Button nextButton = view.findViewById(R.id.nextButtonStayConnected);
        nextButton.setOnClickListener(v -> {
            saveNotificationsToBackend(view);
        });

        TextView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        return view;
    }

    private void saveNotificationsToBackend(View view) {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        // UI Element References
        android.widget.AutoCompleteTextView timeInput = view.findViewById(R.id.timeAutoComplete);
        androidx.appcompat.widget.SwitchCompat dailySwitch = view.findViewById(R.id.switch1);
        androidx.appcompat.widget.SwitchCompat moodSwitch = view.findViewById(R.id.switch2);
        androidx.appcompat.widget.SwitchCompat insightsSwitch = view.findViewById(R.id.switch3);
        androidx.appcompat.widget.SwitchCompat emergencySwitch = view.findViewById(R.id.switch4);
        
        java.util.Map<String, Object> profileUpdates = new java.util.HashMap<>();
        
        if (timeInput != null) profileUpdates.put("notification_time", timeInput.getText().toString());
        if (dailySwitch != null) profileUpdates.put("notifications_enabled", dailySwitch.isChecked());
        if (moodSwitch != null) profileUpdates.put("mood_alerts_enabled", moodSwitch.isChecked());
        if (insightsSwitch != null) profileUpdates.put("weekly_insights_enabled", insightsSwitch.isChecked());
        if (emergencySwitch != null) profileUpdates.put("emergency_alerts_enabled", emergencySwitch.isChecked());

        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .updateUserProfile(userName, profileUpdates)
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        android.widget.Toast.makeText(requireContext(), "Preferences saved", android.widget.Toast.LENGTH_SHORT).show();
                    }
                    Navigation.findNavController(view).navigate(R.id.action_stayConnectedFragment_to_profileOverviewFragment);
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    Navigation.findNavController(view).navigate(R.id.action_stayConnectedFragment_to_profileOverviewFragment);
                }
            });
    }
}