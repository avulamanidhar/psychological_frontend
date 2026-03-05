package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.initialLogoFragment);
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