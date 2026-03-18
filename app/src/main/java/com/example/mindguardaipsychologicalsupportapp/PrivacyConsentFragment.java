package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyConsentFragment extends Fragment {

    private SwitchCompat switchEssential, switchAnalytics;
    private MindGuardApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy_consent, container, false);

        apiService = RetrofitClient.getApiService();
        switchEssential = view.findViewById(R.id.switch1);
        switchAnalytics = view.findViewById(R.id.switch2);

        Button acceptButton = view.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(v -> {
            boolean analyticsEnabled = switchAnalytics.isChecked();
            
            // Save consent locally to be sent during registration
            SharedPreferences prefs = requireActivity().getSharedPreferences("Consent", Context.MODE_PRIVATE);
            prefs.edit()
                .putBoolean("privacy_accepted", true)
                .putBoolean("essential_data", true)
                .putBoolean("anonymous_analytics", analyticsEnabled)
                .putString("policy_version", "1.0.0")
                .apply();

            Navigation.findNavController(view).navigate(R.id.action_privacyConsentFragment_to_createAccountFragment);
        });

        TextView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        // Fetch policy version from backend (optional but professional)
        fetchPolicyInfo();

        return view;
    }

    private void fetchPolicyInfo() {
        apiService.getPrivacyPolicy().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String version = (String) response.body().get("version");
                    // Optionally update SharedPreferences with the latest version from backend
                    SharedPreferences prefs = requireActivity().getSharedPreferences("Consent", Context.MODE_PRIVATE);
                    prefs.edit().putString("policy_version", version).apply();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                // Ignore failure for pre-reg screen to ensure UX is not blocked
            }
        });
    }
}
