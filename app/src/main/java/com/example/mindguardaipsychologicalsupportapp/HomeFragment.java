package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HomeFragment extends Fragment {

    private TextView txtGreeting, txtMentalHealthScore, txtLatestMoodName, txtRecommendation, txtDailyWellnessScore;
    private String userName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtGreeting = view.findViewById(R.id.txtGreeting);
        txtMentalHealthScore = view.findViewById(R.id.txtMentalHealthScore);
        txtDailyWellnessScore = view.findViewById(R.id.txtDailyWellnessScore);
        txtLatestMoodName = view.findViewById(R.id.txtLatestMoodName);
        txtRecommendation = view.findViewById(R.id.txtRecommendation);

        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        userName = prefs.getString("user_name", "User");

         // Set personalized greeting
        if (txtGreeting != null) {
            txtGreeting.setText("Good morning, " + userName + " 👋");
        }

        // Navigation to Notifications
        View btnNotifications = view.findViewById(R.id.btnNotifications);
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_notificationsFragment);
            });
        }

        // Navigation to Mental Health Score
        View wellnessScoreCard = view.findViewById(R.id.cardWellnessScore);
        if (wellnessScoreCard != null) {
            wellnessScoreCard.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_mentalHealthScoreFragment);
            });
        }

        // Navigation to Mood Selection
        View.OnClickListener toMoodSelection = v -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_moodSelectionFragment);
        };
        
        View todaysMoodCard = view.findViewById(R.id.cardTodaysMood);
        if (todaysMoodCard != null) todaysMoodCard.setOnClickListener(toMoodSelection);

        View btnLogMood = view.findViewById(R.id.btnLogMood);
        if (btnLogMood != null) btnLogMood.setOnClickListener(toMoodSelection);

        View btnNavMood = view.findViewById(R.id.btnNavMood);
        if (btnNavMood != null) btnNavMood.setOnClickListener(toMoodSelection);

        // Navigation to Chat
        View btnNavChat = view.findViewById(R.id.btnNavChat);
        if (btnNavChat != null) {
            btnNavChat.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_chatFragment);
            });
        }
        
        View btnChatWithAI = view.findViewById(R.id.btnChatWithAI);
        if (btnChatWithAI != null) {
            btnChatWithAI.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_chatFragment);
            });
        }

        // Navigation to Tools
        View.OnClickListener toTools = v -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_toolsFragment);
        };

        View btnNavTools = view.findViewById(R.id.btnNavTools);
        if (btnNavTools != null) btnNavTools.setOnClickListener(toTools);

        View btnTryNow = view.findViewById(R.id.btnTryNow);
        if (btnTryNow != null) btnTryNow.setOnClickListener(toTools);

        View btnRelax = view.findViewById(R.id.btnRelax);
        if (btnRelax != null) btnRelax.setOnClickListener(toTools);

        // Navigation to AI Insights
        View.OnClickListener toAIInsights = v -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_aiInsightsFragment);
        };

        View btnInsights = view.findViewById(R.id.btnInsights);
        if (btnInsights != null) btnInsights.setOnClickListener(toAIInsights);

        View btnViewFullAnalysis = view.findViewById(R.id.btnViewFullAnalysis);
        if (btnViewFullAnalysis != null) btnViewFullAnalysis.setOnClickListener(toAIInsights);

        // Navigation to Settings (Profile)
        View btnNavProfile = view.findViewById(R.id.btnNavProfile);
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_settingsFragment);
            });
        }

        // Fetch Real-time Dashboard Data
        fetchDashboardData();

        // Demo Alerts
        View btnAlertMild = view.findViewById(R.id.btnAlertMild);
        if (btnAlertMild != null) btnAlertMild.setOnClickListener(v -> showMildAlertBottomSheet());

        View btnAlertModerate = view.findViewById(R.id.btnAlertModerate);
        if (btnAlertModerate != null) btnAlertModerate.setOnClickListener(v -> showModerateAlertBottomSheet());

        View btnAlertHigh = view.findViewById(R.id.btnAlertHigh);
        if (btnAlertHigh != null) btnAlertHigh.setOnClickListener(v -> showHighAlertBottomSheet());

        return view;
    }

    private void fetchDashboardData() {
        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService api = com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService(requireContext());
        api.getDashboardSummary(userName).enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    java.util.Map<String, Object> data = response.body();
                    
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        // Update Greeting
                        if (txtGreeting != null && data.containsKey("greeting")) {
                            txtGreeting.setText(data.get("greeting").toString() + " 👋");
                        }

                        // Update Mental Health Score
                        if (data.containsKey("mental_health_score")) {
                            Object scoreObj = data.get("mental_health_score");
                            String scoreStr = String.valueOf(scoreObj);
                            if (txtMentalHealthScore != null) txtMentalHealthScore.setText(scoreStr + "%");
                            if (txtDailyWellnessScore != null) txtDailyWellnessScore.setText(scoreStr);
                        }

                        // Update Recommendation
                        if (txtRecommendation != null && data.containsKey("recommended_activity")) {
                            txtRecommendation.setText(data.get("recommended_activity").toString());
                        }

                        // Update Latest Mood
                        if (txtLatestMoodName != null) {
                            java.util.Map<String, Object> latestMood = (java.util.Map<String, Object>) data.get("latest_mood");
                            if (latestMood != null && latestMood.containsKey("moodName")) {
                                txtLatestMoodName.setText(latestMood.get("moodName").toString());
                            } else {
                                txtLatestMoodName.setText("No entries yet");
                            }
                        }
                        
                        // Handle Risk Level Alerts (Optional UI extension)
                        if (data.containsKey("risk_level")) {
                            String risk = data.get("risk_level").toString();
                            if ("High".equalsIgnoreCase(risk)) {
                                // Potentially show high alert automatically or change UI color
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error syncing dashboard", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showMildAlertBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.layout_mild_alert_bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.btnClose).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetView.findViewById(R.id.btnDismiss).setOnClickListener(v -> bottomSheetDialog.dismiss());
        
        bottomSheetView.findViewById(R.id.btnTryBreathing).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_toolBreathingFragment);
        });

        bottomSheetDialog.show();
    }

    private void showModerateAlertBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.layout_moderate_alert_bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.btnClose).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetView.findViewById(R.id.btnDismiss).setOnClickListener(v -> bottomSheetDialog.dismiss());
        
        bottomSheetView.findViewById(R.id.btnStartGrounding).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_toolGroundingFragment);
        });

        bottomSheetDialog.show();
    }

    private void showHighAlertBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.layout_high_alert_bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.btnRemindLater).setOnClickListener(v -> bottomSheetDialog.dismiss());
        
        bottomSheetView.findViewById(R.id.btnContinueAI).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_chatFragment);
        });

        bottomSheetView.findViewById(R.id.btnVideo1).setOnClickListener(v -> {
            openVideo("https://www.youtube.com/watch?v=WWloIAQpMcQ");
        });

        bottomSheetView.findViewById(R.id.btnVideo2).setOnClickListener(v -> {
            openVideo("https://www.youtube.com/watch?v=86m4RLpqw_c");
        });

        bottomSheetView.findViewById(R.id.btnEmergency).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_settingsFragment);
        });

        bottomSheetDialog.show();
    }

    private void openVideo(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}