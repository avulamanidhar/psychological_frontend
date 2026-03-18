package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class KeyIndicatorsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_key_indicators, container, false);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Navigation.findNavController(view).navigateUp();
            });
        }

        fetchKeyIndicators(view);

        return view;
    }

    private void fetchKeyIndicators(View view) {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService api = com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
        api.getKeyIndicators(userName).enqueue(new retrofit2.Callback<java.util.Map<String, java.util.Map<String, Object>>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, java.util.Map<String, Object>>> call, retrofit2.Response<java.util.Map<String, java.util.Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(view, response.body());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, java.util.Map<String, Object>>> call, Throwable t) {}
        });
    }

    private void updateUI(View view, java.util.Map<String, java.util.Map<String, Object>> data) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            updateCard(view, "stress", R.id.progressStress, R.id.txtStressStatus, R.id.txtStressDesc, data);
            updateCard(view, "anxiety", R.id.progressAnxiety, R.id.txtAnxietyStatus, R.id.txtAnxietyDesc, data);
            updateCard(view, "depression", R.id.progressDepression, R.id.txtDepressionStatus, R.id.txtDepressionDesc, data);
            updateCard(view, "stability", R.id.progressStability, R.id.txtStabilityStatus, R.id.txtStabilityDesc, data);
        });
    }

    private void updateCard(View view, String key, int progressId, int statusId, int descId, java.util.Map<String, java.util.Map<String, Object>> data) {
        if (!data.containsKey(key)) return;
        java.util.Map<String, Object> metrics = data.get(key);
        
        com.google.android.material.progressindicator.LinearProgressIndicator progress = view.findViewById(progressId);
        android.widget.TextView status = view.findViewById(statusId);
        android.widget.TextView desc = view.findViewById(descId);

        if (progress != null && metrics.containsKey("progress")) {
            progress.setProgress(((Number) metrics.get("progress")).intValue());
        }
        if (status != null && metrics.containsKey("status")) {
            status.setText(metrics.get("status").toString());
        }
        if (desc != null && metrics.containsKey("desc")) {
            desc.setText(metrics.get("desc").toString());
        }
    }
}