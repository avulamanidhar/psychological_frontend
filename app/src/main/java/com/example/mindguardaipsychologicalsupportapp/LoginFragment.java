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
        
        android.widget.EditText emailField = layoutEmail.getEditText();
        android.widget.EditText passwordField = layoutPassword.getEditText();

        Button signInButton = view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(v -> {
            String email = emailField != null ? emailField.getText().toString().trim() : "";
            String password = passwordField != null ? passwordField.getText().toString().trim() : "";

            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(getContext(), "Please enter both email and password", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            java.util.Map<String, String> credentials = new java.util.HashMap<>();
            credentials.put("username", email); 
            credentials.put("password", password);

            com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
                .login(credentials)
                .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                    @Override
                    public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String username = (String) response.body().get("username");
                            
                            android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
                            prefs.edit().putString("user_name", username).apply();

                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
                        } else {
                            android.widget.Toast.makeText(getContext(), "Login failed: Invalid credentials", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                        android.widget.Toast.makeText(getContext(), "Network error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
        });

        view.findViewById(R.id.forgotPasswordText).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
        });

        view.findViewById(R.id.backButton).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        view.findViewById(R.id.signUpText).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        return view;
    }
}