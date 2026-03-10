package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackContactFragment extends Fragment {

    private EditText editSubject, editMessage;
    private RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_contact, container, false);

        editSubject = view.findViewById(R.id.editSubject);
        editMessage = view.findViewById(R.id.editMessage);
        ratingBar = view.findViewById(R.id.ratingBar);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        view.findViewById(R.id.btnSubmitFeedback).setOnClickListener(v -> submitFeedback());

        return view;
    }

    private void submitFeedback() {
        String subject = editSubject != null ? editSubject.getText().toString() : "No Subject";
        String message = editMessage != null ? editMessage.getText().toString() : "";
        int rating = ratingBar != null ? (int) ratingBar.getRating() : 5;

        if (message.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");
        
        MindGuardApiService api = RetrofitClient.getApiService();
        
        api.getUserProfile(userName).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Double userIdDouble = (Double) response.body().get("id");
                    int userId = userIdDouble.intValue();
                    
                    Map<String, Object> feedback = new HashMap<>();
                    feedback.put("user", userId);
                    feedback.put("subject", subject);
                    feedback.put("message", message);
                    feedback.put("rating", rating);

                    api.submitFeedback(feedback).enqueue(new Callback<Map<String, Object>>() {
                        @Override
                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Feedback submitted! Thank you.", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getView()).navigateUp();
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                            Toast.makeText(getContext(), "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                 Toast.makeText(getContext(), "Error fetching user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}