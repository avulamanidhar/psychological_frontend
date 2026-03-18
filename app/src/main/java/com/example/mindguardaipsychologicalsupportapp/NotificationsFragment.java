package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private LinearLayout notificationListContainer;
    private TextView txtNoNotifications;
    private MindGuardApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        View welcomeCard = view.findViewById(R.id.welcomeNotificationCard);
        TextView txtWelcome = view.findViewById(R.id.txtWelcomeNotification);
        
        // Retrieve the saved name from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String name = prefs.getString("user_name", "User");

        if (welcomeCard != null) {
            welcomeCard.setVisibility(View.VISIBLE);
        }

        if (txtWelcome != null) {
            txtWelcome.setText("Welcome, " + name + "!");
        }

        apiService = RetrofitClient.getApiService();
        notificationListContainer = view.findViewById(R.id.notificationListContainer);
        txtNoNotifications = view.findViewById(R.id.txtNoNotifications);

        // Fetch Notifications
        fetchNotifications();

        // Footer Navigation
        view.findViewById(R.id.btnNavHome).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.homeFragment));
        view.findViewById(R.id.btnNavMood).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.moodSelectionFragment));
        view.findViewById(R.id.btnNavChat).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.chatFragment));
        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        // Setup settings switches
        setupSettingsSwitches(view);

        return view;
    }

    private void fetchNotifications() {
        apiService.getNotifications().enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    renderNotifications(response.body());
                } else {
                    if (txtNoNotifications != null) txtNoNotifications.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                if (txtNoNotifications != null) txtNoNotifications.setVisibility(View.VISIBLE);
            }
        });
    }

    private void renderNotifications(List<Map<String, Object>> notifications) {
        if (notificationListContainer == null || !isAdded()) return;
        notificationListContainer.removeAllViews();

        if (notifications.isEmpty()) {
            if (txtNoNotifications != null) txtNoNotifications.setVisibility(View.VISIBLE);
            return;
        }

        if (txtNoNotifications != null) txtNoNotifications.setVisibility(View.GONE);

        for (Map<String, Object> notion : notifications) {
            // Check if item_notification layout exists before inflating
            try {
                View itemView = getLayoutInflater().inflate(R.layout.item_notification, notificationListContainer, false);
                
                TextView title = itemView.findViewById(R.id.txtNotifTitle);
                TextView msg = itemView.findViewById(R.id.txtNotifMessage);
                View unreadIndicator = itemView.findViewById(R.id.unreadIndicator);
                ImageView btnDelete = itemView.findViewById(R.id.btnDeleteNotif);

                if (title != null) title.setText((String) notion.get("title"));
                if (msg != null) msg.setText((String) notion.get("message"));

                boolean isRead = Boolean.TRUE.equals(notion.get("is_read"));
                if (unreadIndicator != null) unreadIndicator.setVisibility(isRead ? View.GONE : View.VISIBLE);

                Object idObj = notion.get("id");
                if (idObj instanceof Number) {
                    int id = ((Number) idObj).intValue();

                    itemView.setOnClickListener(v -> {
                        if (!isRead) {
                            markAsRead(id, unreadIndicator);
                        }
                    });

                    if (btnDelete != null) {
                        btnDelete.setOnClickListener(v -> deleteNotification(id, itemView));
                    }
                }

                notificationListContainer.addView(itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void markAsRead(int id, View indicator) {
        apiService.markNotificationAsRead(id).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && indicator != null) {
                    indicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
        });
    }

    private void deleteNotification(int id, View itemView) {
        apiService.deleteNotification(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notificationListContainer.removeView(itemView);
                    if (notificationListContainer.getChildCount() == 0) {
                        if (txtNoNotifications != null) txtNoNotifications.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    private void setupSettingsSwitches(View view) {
        MaterialSwitch switchDaily = view.findViewById(R.id.switchDaily);
        MaterialSwitch switchAlerts = view.findViewById(R.id.switchAlerts);
        // Sync with backend profile settings in a future iteration
    }
}
