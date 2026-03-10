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

        btnReset.setOnClickListener(v -> {
            if (!isEmailSent) {
                // First click: Show success message (2nd pic state)
                if (successCard != null) successCard.setVisibility(View.VISIBLE);
                if (btnReset != null) btnReset.setText("Back to Login");
                if (inputLayoutEmail != null) inputLayoutEmail.setVisibility(View.GONE);
                if (labelEmail != null) labelEmail.setVisibility(View.GONE);
                isEmailSent = true;
            } else {
                // Second click: Go back to login
                Navigation.findNavController(view).navigateUp();
            }
        });

        view.findViewById(R.id.backButton).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        return view;
    }
}