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

    private enum State { EMAIL, OTP, RESET, SUCCESS }
    private State currentState = State.EMAIL;
    private String userIdentifier = "";
    private String otpCode = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        View emailEntryContainer = view.findViewById(R.id.emailEntryContainer);
        View otpEntryContainer = view.findViewById(R.id.otpEntryContainer);
        View resetEntryContainer = view.findViewById(R.id.resetEntryContainer);
        View successCard = view.findViewById(R.id.successCard);
        
        android.widget.TextView subtitleReset = view.findViewById(R.id.subtitleReset);
        android.widget.TextView successTitle = view.findViewById(R.id.successTitle);
        android.widget.TextView successSubtitle = view.findViewById(R.id.successSubtitle);
        
        android.widget.EditText etEmail = view.findViewById(R.id.etEmail);
        android.widget.EditText etOtp = view.findViewById(R.id.etOtp);
        android.widget.EditText etNewPassword = view.findViewById(R.id.etNewPassword);
        android.widget.EditText etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        Button btnAction = view.findViewById(R.id.btnAction);
        android.widget.ProgressBar progressBar = view.findViewById(R.id.progressBar);

        view.findViewById(R.id.backButton).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        btnAction.setOnClickListener(v -> {
            switch (currentState) {
                case EMAIL:
                    handleSendOtp(etEmail, emailEntryContainer, otpEntryContainer, subtitleReset, btnAction, progressBar);
                    break;
                case OTP:
                    handleVerifyOtp(etOtp, otpEntryContainer, resetEntryContainer, subtitleReset, btnAction, progressBar);
                    break;
                case RESET:
                    handleResetPassword(etNewPassword, etConfirmPassword, resetEntryContainer, successCard, subtitleReset, btnAction, successTitle, successSubtitle, progressBar);
                    break;
                case SUCCESS:
                    Navigation.findNavController(view).navigateUp();
                    break;
            }
        });

        return view;
    }

    private void handleSendOtp(android.widget.EditText etEmail, View current, View next, android.widget.TextView subtitle, Button btn, android.widget.ProgressBar progressBar) {
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            android.widget.Toast.makeText(getContext(), "Please enter your email", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        userIdentifier = email;
        java.util.Map<String, String> data = new java.util.HashMap<>();
        data.put("email", email);

        progressBar.setVisibility(View.VISIBLE);
        btn.setEnabled(false);

        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .forgotPassword(data)
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    progressBar.setVisibility(View.GONE);
                    btn.setEnabled(true);
                    if (response.isSuccessful()) {
                        current.setVisibility(View.GONE);
                        next.setVisibility(View.VISIBLE);
                        subtitle.setText("We've sent a 6-digit code to your email. Please enter it below.");
                        btn.setText("Verify OTP  →");
                        currentState = State.OTP;
                    } else {
                        android.widget.Toast.makeText(getContext(), "Error sending OTP", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    btn.setEnabled(true);
                    android.widget.Toast.makeText(getContext(), "Network error", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void handleVerifyOtp(android.widget.EditText etOtp, View current, View next, android.widget.TextView subtitle, Button btn, android.widget.ProgressBar progressBar) {
        String otp = etOtp.getText().toString().trim();
        if (otp.length() != 6) {
            android.widget.Toast.makeText(getContext(), "Please enter 6-digit OTP", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        otpCode = otp;
        java.util.Map<String, String> data = new java.util.HashMap<>();
        data.put("email", userIdentifier);
        data.put("otp", otp);

        progressBar.setVisibility(View.VISIBLE);
        btn.setEnabled(false);

        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .verifyOtp(data)
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    progressBar.setVisibility(View.GONE);
                    btn.setEnabled(true);
                    if (response.isSuccessful()) {
                        current.setVisibility(View.GONE);
                        next.setVisibility(View.VISIBLE);
                        subtitle.setText("Enter your new password below.");
                        btn.setText("Reset Password  →");
                        currentState = State.RESET;
                    } else {
                        android.widget.Toast.makeText(getContext(), "Invalid OTP", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    btn.setEnabled(true);
                    android.widget.Toast.makeText(getContext(), "Network error", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void handleResetPassword(android.widget.EditText etPass, android.widget.EditText etConfirm, View current, View success, android.widget.TextView subtitle, Button btn, android.widget.TextView sTitle, android.widget.TextView sSubtitle, android.widget.ProgressBar progressBar) {
        String pass = etPass.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (pass.isEmpty() || pass.length() < 6) {
            android.widget.Toast.makeText(getContext(), "Password must be at least 6 characters", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(pass)) {
            android.widget.Toast.makeText(getContext(), "Password must have at least one capital letter, one number, and one special character", android.widget.Toast.LENGTH_LONG).show();
            return;
        }

        if (!pass.equals(confirm)) {
            android.widget.Toast.makeText(getContext(), "Passwords do not match", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        java.util.Map<String, String> data = new java.util.HashMap<>();
        data.put("email", userIdentifier);
        data.put("otp", otpCode);
        data.put("new_password", pass);

        progressBar.setVisibility(View.VISIBLE);
        btn.setEnabled(false);

        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .resetPassword(data)
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    progressBar.setVisibility(View.GONE);
                    btn.setEnabled(true);
                    if (response.isSuccessful()) {
                        current.setVisibility(View.GONE);
                        success.setVisibility(View.VISIBLE);
                        sTitle.setText("Password Reset Successful!");
                        sSubtitle.setText("You can now login with your new password.");
                        subtitle.setVisibility(View.GONE);
                        btn.setText("Back to Login");
                        currentState = State.SUCCESS;
                    } else {
                        android.widget.Toast.makeText(getContext(), "Error resetting password", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    btn.setEnabled(true);
                    android.widget.Toast.makeText(getContext(), "Network error", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
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
