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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detected_patterns, container, false);

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
                if (response.isSuccessful() && response.body() != null) {
                    // In a real app, we'd use a RecyclerView. 
                    // For now, these patterns are statically defined in XML layout, 
                    // but we could populate them dynamically if the layout supported it.
                    // Since the layout has hardcoded cards, we'll just log or show a toast for now.
                    // Ideally, we'd update the TextViews in those cards.
                    
                    List<Map<String, Object>> patterns = response.body();
                    if (!patterns.isEmpty()) {
                        // Example: Update first card if IDs were available
                        // TextView title = getView().findViewById(R.id.txtPatternTitle1);
                        // if (title != null) title.setText((String) patterns.get(0).get("title"));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load patterns", Toast.LENGTH_SHORT).show();
            }
        });
    }
}