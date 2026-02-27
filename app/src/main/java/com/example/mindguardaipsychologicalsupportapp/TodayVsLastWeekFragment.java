package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class TodayVsLastWeekFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_vs_last_week, container, false);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Navigation.findNavController(view).navigateUp();
            });
        }

        // Initialize Comparison Graph with some dummy data
        LineGraphView graphComparison = view.findViewById(R.id.graphComparison);
        if (graphComparison != null) {
            float[] currentWeek = {0.4f, 0.35f, 0.5f, 0.55f, 0.6f, 0.7f, 0.65f};
            float[] lastWeek = {0.45f, 0.45f, 0.35f, 0.3f, 0.28f, 0.25f, 0.22f};
            graphComparison.setData(currentWeek, getResources().getColor(R.color.button_blue));
            graphComparison.setComparisonData(lastWeek);
        }

        return view;
    }
}