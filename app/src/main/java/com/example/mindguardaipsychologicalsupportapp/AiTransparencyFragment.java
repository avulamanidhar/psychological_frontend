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

public class AiTransparencyFragment extends Fragment {

    private TextView txtHowAiWorks, txtLimitations, txtDataUsage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ai_transparency, container, false);

        txtHowAiWorks = view.findViewById(R.id.txtHowAiWorks);
        txtLimitations = view.findViewById(R.id.txtLimitations);
        txtDataUsage = view.findViewById(R.id.txtDataUsage);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        }

        // Bottom Navigation Listeners
        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> {
                Navigation.findNavController(view).popBackStack(R.id.homeFragment, false);
            });
        }

        loadTransparencyData();

        return view;
    }

    private void loadTransparencyData() {
        MindGuardApiService api = RetrofitClient.getApiService();
        api.getAiTransparency().enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    displayTransparency(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                // Silently fail or show error
            }
        });
    }

    private void displayTransparency(List<Map<String, Object>> dataList) {
        for (Map<String, Object> item : dataList) {
            String key = (String) item.get("section_key");
            String content = (String) item.get("content");
            
            if ("how_it_works".equals(key) && txtHowAiWorks != null) {
                txtHowAiWorks.setText(content);
            } else if ("limitations".equals(key) && txtLimitations != null) {
                txtLimitations.setText(content);
            } else if ("data_usage".equals(key) && txtDataUsage != null) {
                txtDataUsage.setText(content);
            }
        }
    }
}
