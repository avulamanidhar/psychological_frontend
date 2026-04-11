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
import com.example.mindguardaipsychologicalsupportapp.utils.SessionManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Account Section
        View cardAccount = view.findViewById(R.id.cardAccountInfo);
        if (cardAccount != null) {
            cardAccount.setOnClickListener(v -> 
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_accountInfoFragment));
        }

        View cardPrivacy = view.findViewById(R.id.cardPrivacySecurity);
        if (cardPrivacy != null) {
            cardPrivacy.setOnClickListener(v -> 
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_privacySecurityFragment));
        }

        // App Section
        View cardAi = view.findViewById(R.id.cardAiTransparency);
        if (cardAi != null) {
            cardAi.setOnClickListener(v -> 
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_aiTransparencyFragment));
        }

        View cardNotif = view.findViewById(R.id.cardNotifications);
        if (cardNotif != null) {
            cardNotif.setOnClickListener(v -> 
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_notificationsFragment));
        }

        // Support Section
        View cardHelp = view.findViewById(R.id.cardHelpFaq);
        if (cardHelp != null) {
            cardHelp.setOnClickListener(v -> 
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_helpFaqFragment));
        }

        View cardFeedback = view.findViewById(R.id.cardFeedback);
        if (cardFeedback != null) {
            cardFeedback.setOnClickListener(v -> 
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_feedbackContactFragment));
        }

        // Log Out
        View logoutBtn = view.findViewById(R.id.btnLogout);
        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(v -> {
                SessionManager.logoutAndClear(view, "Logged out successfully");
            });
        }

        // Delete Account
        View deleteAccountBtn = view.findViewById(R.id.btnDeleteAccount);
        if (deleteAccountBtn != null) {
            deleteAccountBtn.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account? This will permanently remove all your data.")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        RetrofitClient.getApiService(requireContext()).deleteUserData().enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    SessionManager.logoutAndClear(view, "Account deleted successfully");
                                } else {
                                    Toast.makeText(requireContext(), "Failed to delete account: " + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            });
        }
        
        TextView appVersionText = view.findViewById(R.id.appVersionText);
        if (appVersionText != null) {
            MindGuardApiService apiService = RetrofitClient.getApiService();
            apiService.getSystemStatus().enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String version = (String) response.body().get("version");
                        if (version != null) {
                            appVersionText.setText("Version " + version);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    // Retain default version text
                }
            });
        }

        // Bottom Navigation Listeners
        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_homeFragment);
            });
        }

        View btnNavMood = view.findViewById(R.id.btnNavMood);
        if (btnNavMood != null) {
            btnNavMood.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_moodSelectionFragment);
            });
        }

        View btnNavChat = view.findViewById(R.id.btnNavChat);
        if (btnNavChat != null) {
            btnNavChat.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_chatFragment);
            });
        }

        View btnNavTools = view.findViewById(R.id.btnNavTools);
        if (btnNavTools != null) {
            btnNavTools.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_toolsFragment);
            });
        }

        return view;
    }
}