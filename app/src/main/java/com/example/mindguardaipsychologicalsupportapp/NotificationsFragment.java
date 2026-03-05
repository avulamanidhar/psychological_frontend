package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class NotificationsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        View welcomeCard = view.findViewById(R.id.welcomeNotificationCard);
        TextView txtWelcome = view.findViewById(R.id.txtWelcomeNotification);
        
        // Retrieve the saved name from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String name = prefs.getString("user_name", "User");

        if (welcomeCard != null) {
            welcomeCard.setVisibility(View.VISIBLE);
        }

        if (txtWelcome != null) {
            txtWelcome.setText("Welcome, " + name + "!");
        }

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        return view;
    }
}