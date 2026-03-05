package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class FeedbackContactFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_contact, container, false);
        
        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        
        view.findViewById(R.id.btnSubmitFeedback).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Feedback submitted! Thank you.", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigateUp();
        });

        return view;
    }
}