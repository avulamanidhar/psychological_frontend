package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoodTrendsFragment extends Fragment {

    private LineGraphView graphMoodScore, graphStressAnxiety;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_trends, container, false);

        graphMoodScore = view.findViewById(R.id.graphMoodScore);
        graphStressAnxiety = view.findViewById(R.id.graphStressAnxiety);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Navigation.findNavController(view).navigateUp();
            });
        }

        loadTrendData();

        return view;
    }

    private void loadTrendData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        MindGuardApiService api = RetrofitClient.getApiService();
        api.getInsightTrends(userName).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    
                    List<Double> moodList = (List<Double>) data.get("mood_scores");
                    List<Double> stressList = (List<Double>) data.get("stress_scores");
                    List<Double> anxietyList = (List<Double>) data.get("anxiety_scores");

                    if (graphMoodScore != null && moodList != null) {
                        float[] points = new float[moodList.size()];
                        for (int i = 0; i < moodList.size(); i++) points[i] = moodList.get(i).floatValue();
                        graphMoodScore.setData(points, Color.parseColor("#4A90E2"));
                    }

                    if (graphStressAnxiety != null && stressList != null && anxietyList != null) {
                        float[] sPoints = new float[stressList.size()];
                        for (int i = 0; i < stressList.size(); i++) sPoints[i] = stressList.get(i).floatValue();
                        
                        float[] aPoints = new float[anxietyList.size()];
                        for (int i = 0; i < anxietyList.size(); i++) aPoints[i] = anxietyList.get(i).floatValue();
                        
                        graphStressAnxiety.setData(sPoints, Color.parseColor("#FF6B6B"));
                        // LineGraphView currently only supports one set of data points in setData, 
                        // but it supports comparison data.
                        graphStressAnxiety.setComparisonData(aPoints);
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load trends", Toast.LENGTH_SHORT).show();
            }
        });
    }
}