package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ForgotPasswordFragment extends Fragment {

    private boolean isEmailSent = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        View successCard = view.findViewById(R.id.successCard);
        Button btnReset = view.findViewById(R.id.btnSendResetLink);
        View inputLayoutEmail = view.findViewById(R.id.inputLayoutEmail);
        View labelEmail = view.findViewById(R.id.labelEmail);
        android.widget.EditText etEmail = view.findViewById(R.id.etEmail);

        btnReset.setOnClickListener(v -> {
            if (!isEmailSent) {
                String email = etEmail != null ? etEmail.getText().toString().trim() : "";
                if (email.isEmpty()) {
                    android.widget.Toast.makeText(getContext(), "Please enter your email", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }

                java.util.Map<String, String> data = new java.util.HashMap<>();
                data.put("username", email); 

                com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
                    .forgotPassword(data)
                    .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                        @Override
                        public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                            if (response.isSuccessful()) {
                                if (successCard != null) successCard.setVisibility(View.VISIBLE);
                                if (btnReset != null) btnReset.setText("Back to Login");
                                if (inputLayoutEmail != null) inputLayoutEmail.setVisibility(View.GONE);
                                if (labelEmail != null) labelEmail.setVisibility(View.GONE);
                                isEmailSent = true;
                            } else {
                                android.widget.Toast.makeText(getContext(), "Error: User not found", android.widget.Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                            android.widget.Toast.makeText(getContext(), "Network error", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });
            } else {
                // Second click: Go back to login
                Navigation.findNavController(view).navigateUp();
            }
        });

        view.findViewById(R.id.backButton).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        return view;
    }
}