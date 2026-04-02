package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        com.google.android.material.textfield.TextInputLayout layoutEmail = view.findViewById(R.id.inputLayoutEmail);
        com.google.android.material.textfield.TextInputLayout layoutPassword = view.findViewById(R.id.inputLayoutPassword);
        
        android.widget.EditText emailField = layoutEmail != null ? layoutEmail.getEditText() : null;
        android.widget.EditText passwordField = layoutPassword != null ? layoutPassword.getEditText() : null;

        Button signInButton = view.findViewById(R.id.signInButton);
        if (signInButton != null) {
            signInButton.setOnClickListener(v -> {
                String email = emailField != null ? emailField.getText().toString().trim() : "";
                String password = passwordField != null ? passwordField.getText().toString().trim() : "";

                if (email.isEmpty() || password.isEmpty()) {
                    android.widget.Toast.makeText(getContext(), "Please enter both email and password", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    android.widget.Toast.makeText(getContext(), "Password must be at least 8 characters long", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                java.util.Map<String, String> credentials = new java.util.HashMap<>();
                credentials.put("username", email); 
                credentials.put("password", password);

                com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
                    .login(credentials)
                    .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                        @Override
                        public void onResponse(@NonNull retrofit2.Call<java.util.Map<String, Object>> call, @NonNull retrofit2.Response<java.util.Map<String, Object>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String username = (String) response.body().get("username");
                                String accessToken = (String) response.body().get("access");
                                String refreshToken = (String) response.body().get("refresh");
                                
                                android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
                                prefs.edit()
                                    .putString("user_name", username)
                                    .putString("auth_token", accessToken)
                                    .putString("refresh_token", refreshToken)
                                    .apply();

                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
                            } else {
                                android.widget.Toast.makeText(getContext(), "Login failed: Invalid credentials", android.widget.Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull retrofit2.Call<java.util.Map<String, Object>> call, @NonNull Throwable t) {
                            android.widget.Toast.makeText(getContext(), "Network error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });
            });
        }

        View forgotPassword = view.findViewById(R.id.forgotPasswordText);
        if (forgotPassword != null) {
            forgotPassword.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            });
        }

        View backBtn = view.findViewById(R.id.backButton);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        }

        View signUpText = view.findViewById(R.id.signUpText);
        if (signUpText != null) {
            signUpText.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        }

        return view;
    }
}
