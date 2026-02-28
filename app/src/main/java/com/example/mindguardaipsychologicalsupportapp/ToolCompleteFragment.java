package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ToolCompleteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_complete, container, false);

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
        if (btnNavProfile != null) btnNavProfile.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolCompleteFragment_to_profileOverviewFragment));

        return view;
    }
}
