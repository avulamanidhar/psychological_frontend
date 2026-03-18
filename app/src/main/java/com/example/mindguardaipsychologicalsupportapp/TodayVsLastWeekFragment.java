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

    private LineGraphView graphComparison;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_vs_last_week, container, false);

        graphComparison = view.findViewById(R.id.graphComparison);
        
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        }

        fetchComparisonData(view);

        return view;
    }

    private void fetchComparisonData(View view) {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService api = com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
        api.getMoodTrendGraph(userName).enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null && graphComparison != null) {
                    java.util.Map<String, Object> body = response.body();

                    try {
                        java.util.List<Double> current = (java.util.List<Double>) body.get("current_points");
                        java.util.List<Double> comparison = (java.util.List<Double>) body.get("comparison_points");

                        if (current != null) {
                            float[] currentArr = new float[current.size()];
                            for (int i = 0; i < current.size(); i++) currentArr[i] = current.get(i).floatValue();
                            graphComparison.setData(currentArr, androidx.core.content.ContextCompat.getColor(requireContext(), R.color.button_blue));
                        }

                        if (comparison != null) {
                            float[] compArr = new float[comparison.size()];
                            for (int i = 0; i < comparison.size(); i++) compArr[i] = comparison.get(i).floatValue();
                            graphComparison.setComparisonData(compArr);
                        }
                    } catch (Exception e) {}

                    // Update UI Labels
                    android.widget.TextView txtMoodScore = view.findViewById(R.id.txtMoodScore);
                    android.widget.TextView txtMoodTrend = view.findViewById(R.id.txtMoodTrend);
                    if (txtMoodScore != null && body.containsKey("mood_score")) txtMoodScore.setText(formatStat(body.get("mood_score")));
                    if (txtMoodTrend != null && body.containsKey("mood_trend")) txtMoodTrend.setText(String.valueOf(body.get("mood_trend")));

                    android.widget.TextView txtAnxietyScore = view.findViewById(R.id.txtAnxietyScore);
                    android.widget.TextView txtAnxietyTrend = view.findViewById(R.id.txtAnxietyTrend);
                    if (txtAnxietyScore != null && body.containsKey("anxiety_score")) txtAnxietyScore.setText(formatStat(body.get("anxiety_score")) + "%");
                    if (txtAnxietyTrend != null && body.containsKey("anxiety_trend")) txtAnxietyTrend.setText(String.valueOf(body.get("anxiety_trend")));

                    android.widget.TextView txtStressScore = view.findViewById(R.id.txtStressScore);
                    android.widget.TextView txtStressTrend = view.findViewById(R.id.txtStressTrend);
                    if (txtStressScore != null && body.containsKey("stress_score")) txtStressScore.setText(formatStat(body.get("stress_score")) + "%");
                    if (txtStressTrend != null && body.containsKey("stress_trend")) txtStressTrend.setText(String.valueOf(body.get("stress_trend")));

                    android.widget.TextView txtSleepScore = view.findViewById(R.id.txtSleepScore);
                    android.widget.TextView txtSleepTrend = view.findViewById(R.id.txtSleepTrend);
                    if (txtSleepScore != null && body.containsKey("sleep_score")) txtSleepScore.setText(formatStat(body.get("sleep_score")));
                    if (txtSleepTrend != null && body.containsKey("sleep_trend")) txtSleepTrend.setText(String.valueOf(body.get("sleep_trend")));
                }
            }

            private String formatStat(Object obj) {
                if (obj instanceof Number) {
                    return String.valueOf(((Number) obj).intValue());
                }
                return String.valueOf(obj);
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                if (getContext() != null) {
                    android.widget.Toast.makeText(getContext(), "Failed to fetch analytics", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}