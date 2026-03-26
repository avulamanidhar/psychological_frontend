package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class CreateAccountFragment extends Fragment {

    private EditText etFullName, etEmail, etPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        Button createAccountButton = view.findViewById(R.id.createAccountButton);
        TextView loginText = view.findViewById(R.id.loginText);
        
        if (createAccountButton != null) {
            createAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etFullName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (name.length() <= 1) {
                        Toast.makeText(getContext(), "Name must be more than one character", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!email.contains("@")) {
                        Toast.makeText(getContext(), "Please enter a valid email address with @", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!isValidPassword(password)) {
                        Toast.makeText(getContext(), "Password must have at least one capital letter, one number, and one special character", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Create user map for API
                    java.util.HashMap<String, Object> userMap = new java.util.HashMap<>();
                    userMap.put("username", name);
                    userMap.put("password", password);
                    userMap.put("email", email);

                    // Fetch consent choices from SharedPreferences
                    SharedPreferences consentPrefs = requireActivity().getSharedPreferences("Consent", Context.MODE_PRIVATE);
                    userMap.put("privacy_consent_accepted", consentPrefs.getBoolean("privacy_accepted", false));
                    userMap.put("essential_data_processing", consentPrefs.getBoolean("essential_data", true));
                    userMap.put("anonymous_analytics", consentPrefs.getBoolean("anonymous_analytics", false));
                    userMap.put("privacy_policy_version", consentPrefs.getString("policy_version", "1.0.0"));

                    com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
                        .registerUser(userMap)
                        .enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
                            @Override
                            public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    // Save user name in SharedPreferences
                                    SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                                    prefs.edit().putString("user_name", name).apply();

                                    // Navigation to Get To Know You screen
                                    try {
                                        Navigation.findNavController(view).navigate(R.id.action_createAccountFragment_to_getToKnowYouFragment);
                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), "Navigation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    try {
                                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                                        Toast.makeText(getContext(), "Registration failed: " + errorBody, Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), "Registration failed: " + response.message(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            });
        }

        if (loginText != null) {
            loginText.setOnClickListener(v -> {
                try {
                    Navigation.findNavController(view).navigate(R.id.action_createAccountFragment_to_loginFragment);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Navigation to login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        TextView backButton = view.findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        }

        return view;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 6) return false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        String specialChars = "!@#$%^&*()-_+=<>?/{}[]|";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (specialChars.indexOf(c) != -1) hasSpecial = true;
        }
        return hasUpper && hasDigit && hasSpecial;
    }

}
