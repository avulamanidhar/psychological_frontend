package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class AIAnalysisFragment extends Fragment {

    private TextView txtAnalysisTitle, txtAnalysisSubtitle;
    private TextView txtStep1Title, txtStep1Desc;
    private TextView txtStep2Title, txtStep2Desc;
    private TextView txtStep3Title, txtStep3Desc;
    private TextView txtStep4Title, txtStep4Desc;
    private TextView txtConfidenceLevel, txtDataPoints;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ai_analysis, container, false);

        initializeViews(view);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Navigation.findNavController(view).navigateUp();
            });
        }

        loadAnalysisData();

        return view;
    }

    private void initializeViews(View view) {
        txtAnalysisTitle = view.findViewById(R.id.txtAnalysisTitle);
        txtAnalysisSubtitle = view.findViewById(R.id.txtAnalysisSubtitle);
        txtStep1Title = view.findViewById(R.id.txtStep1Title);
        txtStep1Desc = view.findViewById(R.id.txtStep1Desc);
        txtStep2Title = view.findViewById(R.id.txtStep2Title);
        txtStep2Desc = view.findViewById(R.id.txtStep2Desc);
        txtStep3Title = view.findViewById(R.id.txtStep3Title);
        txtStep3Desc = view.findViewById(R.id.txtStep3Desc);
        txtStep4Title = view.findViewById(R.id.txtStep4Title);
        txtStep4Desc = view.findViewById(R.id.txtStep4Desc);
        txtConfidenceLevel = view.findViewById(R.id.txtConfidenceLevel);
        txtDataPoints = view.findViewById(R.id.txtDataPoints);
    }

    private void loadAnalysisData() {
        MindGuardApiService api = RetrofitClient.getApiService();
        api.getAIAnalysis().enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Map<String, Object> analysis = response.body().get(0);
                    displayAnalysis(analysis);
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                // Silently fail or show error
            }
        });
    }

    private void displayAnalysis(Map<String, Object> analysis) {
        if (txtAnalysisTitle != null) txtAnalysisTitle.setText((String) analysis.get("title"));
        if (txtAnalysisSubtitle != null) txtAnalysisSubtitle.setText((String) analysis.get("subtitle"));

        List<Map<String, Object>> steps = (List<Map<String, Object>>) analysis.get("steps");
        if (steps != null && steps.size() >= 4) {
            if (txtStep1Title != null) {
                txtStep1Title.setText((String) steps.get(0).get("title"));
                txtStep1Desc.setText((String) steps.get(0).get("description"));
            }
            if (txtStep2Title != null) {
                txtStep2Title.setText((String) steps.get(1).get("title"));
                txtStep2Desc.setText((String) steps.get(1).get("description"));
            }
            if (txtStep3Title != null) {
                txtStep3Title.setText((String) steps.get(2).get("title"));
                txtStep3Desc.setText((String) steps.get(2).get("description"));
            }
            if (txtStep4Title != null) {
                txtStep4Title.setText((String) steps.get(3).get("title"));
                txtStep4Desc.setText((String) steps.get(3).get("description"));
            }
        }

        if (txtConfidenceLevel != null) {
            Object confObj = analysis.get("confidence_level");
            int level = confObj instanceof Double ? ((Double) confObj).intValue() : (int) confObj;
            txtConfidenceLevel.setText("Confidence Level: " + level + "%");
        }

        if (txtDataPoints != null) {
            Object dpObj = analysis.get("data_points_count");
            int points = dpObj instanceof Double ? ((Double) dpObj).intValue() : (int) dpObj;
            Object wkObj = analysis.get("weeks_count");
            int weeks = wkObj instanceof Double ? ((Double) wkObj).intValue() : (int) wkObj;
            txtDataPoints.setText("Based on " + points + " data points over " + weeks + " weeks");
        }
    }
}