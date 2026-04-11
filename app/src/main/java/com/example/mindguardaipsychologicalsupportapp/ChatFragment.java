package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.api.ChatBotApiService;
import com.example.mindguardaipsychologicalsupportapp.api.ChatRetrofitClient;
import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;
import com.example.mindguardaipsychologicalsupportapp.utils.SessionManager;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    private LinearLayout chatMessagesContainer;
    private ScrollView chatScrollView;
    private EditText etChatMessage;
    private String selectedLanguage;
    private String userName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatMessagesContainer = view.findViewById(R.id.chatMessagesContainer);
        chatScrollView = view.findViewById(R.id.chatScrollView);
        etChatMessage = view.findViewById(R.id.etChatMessage);
        View btnSend = view.findViewById(R.id.btnSendChat);

        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        selectedLanguage = prefs.getString("selected_language", "English");
        userName = prefs.getString("user_name", "User");

        if (btnSend != null) {
            btnSend.setOnClickListener(v -> sendMessage());
        }

        ViewGroup chipsContainer = (ViewGroup) ((ViewGroup) view.findViewById(R.id.quickSuggestions)).getChildAt(0);
        for (int i = 0; i < chipsContainer.getChildCount(); i++) {
            View child = chipsContainer.getChildAt(i);
            if (child instanceof Chip) {
                ((Chip) child).setOnClickListener(v -> {
                    String suggestion = ((Chip) v).getText().toString();
                    etChatMessage.setText(""); // clear any existing text
                    addUserMessage(suggestion, false);
                    sendApiMessage(suggestion);
                });
            }
        }

        loadChatHistory();

        view.findViewById(R.id.btnNavHome).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_homeFragment));
        view.findViewById(R.id.btnNavMood).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_moodSelectionFragment));
        view.findViewById(R.id.btnNavTools).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_toolsFragment));
        view.findViewById(R.id.btnNavProfile).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_settingsFragment));

        return view;
    }

    private void loadChatHistory() {
        MindGuardApiService api = RetrofitClient.getApiService(requireContext());
        api.getChatHistory(userName).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    for (Map<String, Object> msg : response.body()) {
                        boolean isUser = (boolean) msg.get("is_user");
                        String text = (String) msg.get("text");
                        if (isUser) {
                            addUserMessage(text, false);
                        } else {
                            addAssistantMessage(text, false);
                        }
                    }
                } else if (response.code() == 401) {
                    SessionManager.logoutAndClear(requireView(), "Session expired. Please log in again.");
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                // No history or failed to load history
            }
        });
    }

    private void sendMessage() {
        String msg = etChatMessage.getText().toString().trim();
        if (msg.isEmpty()) return;

        etChatMessage.setText("");
        addUserMessage(msg, false); // Display locally first

        sendApiMessage(msg);
    }
    
    private void sendApiMessage(String msg) {
        // Show thinking state (simulated)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            MindGuardApiService api = RetrofitClient.getApiService(requireContext());
            Map<String, Object> payload = new HashMap<>();
            payload.put("text", msg);
            payload.put("mode", "General"); // Can be dynamic based on UI in future
            payload.put("language", selectedLanguage);

            api.sendChatMessage(payload).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String aiText = (String) response.body().get("reply");
                        if (aiText != null) {
                            addAssistantMessage(aiText, false);
                        } else {
                            addAssistantMessage("Error: AI response is empty.", false);
                        }
                    } else if (response.code() == 401) {
                        if (isAdded() && getView() != null) {
                            SessionManager.logoutAndClear(requireView(), "Session expired. Please log in again.");
                        }
                    } else {
                        addAssistantMessage("Error: Failed to connect to MindGuard backend (" + response.code() + ")", false);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    addAssistantMessage("Error: Backend is unreachable (" + t.getMessage() + ")", false);
                }
            });
        }, 500); 
    }

    private void saveMessage(String text, boolean isUser) {
        // This method is now redundant as the backend handles saving both user and AI messages in one POST call.
        // It's kept for backward compatibility if needed, but not used in the new flow.
    }



    private void addUserMessage(String text, boolean save) {
        View userMsgView = getLayoutInflater().inflate(R.layout.item_chat_user, chatMessagesContainer, false);
        ((TextView) userMsgView.findViewById(R.id.txtMessage)).setText(text);
        ((TextView) userMsgView.findViewById(R.id.txtTime)).setText(getCurrentTime());
        chatMessagesContainer.addView(userMsgView);
        scrollToBottom();
        if (save) saveMessage(text, true);
    }

    private void addAssistantMessage(String text, boolean save) {
        View assistantMsgView = getLayoutInflater().inflate(R.layout.item_chat_assistant, chatMessagesContainer, false);
        ((TextView) assistantMsgView.findViewById(R.id.txtMessage)).setText(text);
        ((TextView) assistantMsgView.findViewById(R.id.txtTime)).setText(getCurrentTime());
        chatMessagesContainer.addView(assistantMsgView);
        scrollToBottom();
        if (save) saveMessage(text, false);
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
    }

    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
    }
}
