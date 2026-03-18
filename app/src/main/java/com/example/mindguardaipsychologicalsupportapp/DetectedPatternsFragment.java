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

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetectedPatternsFragment extends Fragment {

    private TextView txtTitle1, txtConfidence1, txtDesc1;
    private TextView txtTitle2, txtConfidence2, txtDesc2;
    private TextView txtTitle3, txtConfidence3, txtDesc3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detected_patterns, container, false);

        txtTitle1 = view.findViewById(R.id.txtPatternTitle1);
        txtConfidence1 = view.findViewById(R.id.txtPatternConfidence1);
        txtDesc1 = view.findViewById(R.id.txtPatternDesc1);
        
        txtTitle2 = view.findViewById(R.id.txtPatternTitle2);
        txtConfidence2 = view.findViewById(R.id.txtPatternConfidence2);
        txtDesc2 = view.findViewById(R.id.txtPatternDesc2);
        
        txtTitle3 = view.findViewById(R.id.txtPatternTitle3);
        txtConfidence3 = view.findViewById(R.id.txtPatternConfidence3);
        txtDesc3 = view.findViewById(R.id.txtPatternDesc3);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Navigation.findNavController(view).navigateUp();
            });
        }

        loadPatterns();

        return view;
    }

    private void loadPatterns() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        MindGuardApiService api = RetrofitClient.getApiService();
        api.getInsightPatterns(userName).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    displayPatterns(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Failed to load patterns", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayPatterns(List<Map<String, Object>> patterns) {
        if (patterns.size() >= 1) {
            Map<String, Object> p = patterns.get(0);
            if (txtTitle1 != null) txtTitle1.setText((String) p.get("title"));
            if (txtConfidence1 != null) txtConfidence1.setText((String) p.get("confidence") + " confidence");
            if (txtDesc1 != null) txtDesc1.setText((String) p.get("description"));
        }
        if (patterns.size() >= 2) {
            Map<String, Object> p = patterns.get(1);
            if (txtTitle2 != null) txtTitle2.setText((String) p.get("title"));
            if (txtConfidence2 != null) txtConfidence2.setText((String) p.get("confidence") + " confidence");
            if (txtDesc2 != null) txtDesc2.setText((String) p.get("description"));
        }
        if (patterns.size() >= 3) {
            Map<String, Object> p = patterns.get(2);
            if (txtTitle3 != null) txtTitle3.setText((String) p.get("title"));
            if (txtConfidence3 != null) txtConfidence3.setText((String) p.get("confidence") + " confidence");
            if (txtDesc3 != null) txtDesc3.setText((String) p.get("description"));
        }
    }
}