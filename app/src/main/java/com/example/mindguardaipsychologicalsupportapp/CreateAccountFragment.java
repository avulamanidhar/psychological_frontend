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

    private EditText etFullName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        etFullName = view.findViewById(R.id.etFullName);
        Button createAccountButton = view.findViewById(R.id.createAccountButton);
        TextView loginText = view.findViewById(R.id.loginText);
        
        if (createAccountButton != null) {
            createAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etFullName.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create user map for API
                    java.util.HashMap<String, String> userMap = new java.util.HashMap<>();
                    userMap.put("username", name);
                    userMap.put("password", "password123"); // Default password for demo
                    userMap.put("email", name.toLowerCase().replace(" ", ".") + "@example.com");

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
                                    Toast.makeText(getContext(), "Registration failed: " + response.message(), Toast.LENGTH_SHORT).show();
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
}