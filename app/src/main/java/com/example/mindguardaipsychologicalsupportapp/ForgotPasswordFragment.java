package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        
        TextView subtitleReset = view.findViewById(R.id.subtitleReset);
        TextView successTitle = view.findViewById(R.id.successTitle);
        TextView successSubtitle = view.findViewById(R.id.successSubtitle);
        
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etOtp = view.findViewById(R.id.etOtp);
        EditText etNewPassword = view.findViewById(R.id.etNewPassword);
        EditText etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        Button btnAction = view.findViewById(R.id.btnAction);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

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
                    Navigation.findNavController(view).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
                    break;
            }
        });

        return view;
    }

    private void handleSendOtp(EditText etEmail, View current, View next, TextView subtitle, Button btn, ProgressBar progressBar) {
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        userIdentifier = email;
        Map<String, String> data = new HashMap<>();
        data.put("email", email);

        progressBar.setVisibility(View.VISIBLE);
        btn.setEnabled(false);

        RetrofitClient.getApiService().forgotPassword(data).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                progressBar.setVisibility(View.GONE);
                btn.setEnabled(true);
                if (response.isSuccessful()) {
                    current.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                    subtitle.setText("Step 2: Verification\nWe've sent a 6-digit code to your email. Click below after entering it.");
                    btn.setText("Verify Code  →");
                    currentState = State.OTP;
                    Toast.makeText(getContext(), "OTP sent! Please check your email.", Toast.LENGTH_SHORT).show();
                } else {
                    String msg = "Failed to send OTP";
                    try {
                        if (response.errorBody() != null) {
                            JSONObject obj = new JSONObject(response.errorBody().string());
                            msg = obj.optString("message", msg);
                        }
                    } catch (Exception e) {}
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btn.setEnabled(true);
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleVerifyOtp(EditText etOtp, View current, View next, TextView subtitle, Button btn, ProgressBar progressBar) {
        String otp = etOtp.getText().toString().trim();
        if (otp.length() != 6) {
            Toast.makeText(getContext(), "Enter 6-digit OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        otpCode = otp;
        Map<String, String> data = new HashMap<>();
        data.put("email", userIdentifier);
        data.put("otp", otp);

        progressBar.setVisibility(View.VISIBLE);
        btn.setEnabled(false);

        RetrofitClient.getApiService().verifyOtp(data).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                progressBar.setVisibility(View.GONE);
                btn.setEnabled(true);
                if (response.isSuccessful()) {
                    current.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                    subtitle.setText("Step 3: Create New Password\nAlmost there! Secure your account with a new password.");
                    btn.setText("Update Password");
                    currentState = State.RESET;
                } else {
                    String msg = "Invalid OTP";
                    try {
                        if (response.errorBody() != null) {
                            JSONObject obj = new JSONObject(response.errorBody().string());
                            msg = obj.optString("error", msg);
                        }
                    } catch (Exception e) {}
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btn.setEnabled(true);
                Toast.makeText(getContext(), "Verification failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResetPassword(EditText etPass, EditText etConfirm, View current, View success, TextView subtitle, Button btn, TextView sTitle, TextView sSubtitle, ProgressBar progressBar) {
        String pass = etPass.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (pass.isEmpty() || pass.length() < 6) {
            Toast.makeText(getContext(), "Minimum 6 characters required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(pass)) {
            Toast.makeText(getContext(), "Include 1 capital, 1 number, and 1 special char", Toast.LENGTH_LONG).show();
            return;
        }

        if (!pass.equals(confirm)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> data = new HashMap<>();
        data.put("email", userIdentifier);
        data.put("otp", otpCode);
        data.put("new_password", pass);

        progressBar.setVisibility(View.VISIBLE);
        btn.setEnabled(false);

        RetrofitClient.getApiService().resetPassword(data).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                progressBar.setVisibility(View.GONE);
                btn.setEnabled(true);
                if (response.isSuccessful()) {
                    current.setVisibility(View.GONE);
                    success.setVisibility(View.VISIBLE);
                    sTitle.setText("Password Reset Done! 🎉");
                    sSubtitle.setText("You will be navigated to the login portal shortly...");
                    subtitle.setVisibility(View.GONE);
                    btn.setText("Go to Login");
                    currentState = State.SUCCESS;

                    new Handler().postDelayed(() -> {
                        if (isAdded() && getView() != null) {
                            Navigation.findNavController(getView()).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
                        }
                    }, 3000);
                } else {
                    Toast.makeText(getContext(), "Reset failed. Please retry.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btn.setEnabled(true);
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
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
