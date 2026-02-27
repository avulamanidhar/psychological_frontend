package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ChatFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Bottom Navigation Listeners
        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_homeFragment);
            });
        }

        View btnNavMood = view.findViewById(R.id.btnNavMood);
        if (btnNavMood != null) {
            btnNavMood.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_moodSelectionFragment);
            });
        }

        View btnNavTools = view.findViewById(R.id.btnNavTools);
        if (btnNavTools != null) {
            btnNavTools.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_toolsFragment);
            });
        }

        return view;
    }
}