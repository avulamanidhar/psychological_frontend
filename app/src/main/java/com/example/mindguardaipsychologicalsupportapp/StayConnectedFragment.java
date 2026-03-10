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

        java.util.Map<String, Object> profileUpdates = new java.util.HashMap<>();
        profileUpdates.put("notifications_enabled", true); // Default enabled

        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .updateUserProfile(userName, profileUpdates)
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    Navigation.findNavController(view).navigate(R.id.action_stayConnectedFragment_to_profileOverviewFragment);
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    Navigation.findNavController(view).navigate(R.id.action_stayConnectedFragment_to_profileOverviewFragment);
                }
            });
    }
}