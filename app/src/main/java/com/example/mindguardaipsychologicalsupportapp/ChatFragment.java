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
import com.google.android.material.chip.Chip;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ChatFragment extends Fragment {

    private LinearLayout chatMessagesContainer;
    private ScrollView chatScrollView;
    private EditText etChatMessage;
    private String selectedLanguage;
    private String currentMode = "General"; // Modes: General, Calm, CBT, Listener
    private Random random = new Random();

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

        if (btnSend != null) {
            btnSend.setOnClickListener(v -> sendMessage());
        }

        ViewGroup chipsContainer = (ViewGroup) ((ViewGroup) view.findViewById(R.id.quickSuggestions)).getChildAt(0);
        for (int i = 0; i < chipsContainer.getChildCount(); i++) {
            View child = chipsContainer.getChildAt(i);
            if (child instanceof Chip) {
                ((Chip) child).setOnClickListener(v -> {
                    etChatMessage.setText(((Chip) v).getText().toString());
                    sendMessage();
                });
            }
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            addAssistantMessage(getGreeting(selectedLanguage));
        }, 500);

        view.findViewById(R.id.btnNavHome).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_homeFragment));
        view.findViewById(R.id.btnNavMood).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_moodSelectionFragment));
        view.findViewById(R.id.btnNavTools).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_toolsFragment));
        view.findViewById(R.id.btnNavProfile).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_chatFragment_to_settingsFragment));

        return view;
    }

    private void sendMessage() {
        String msg = etChatMessage.getText().toString().trim();
        if (msg.isEmpty()) return;

        addUserMessage(msg);
        etChatMessage.setText("");

        // Check for mode switches
        if (msg.toLowerCase().contains("switch to calm mode")) {
            currentMode = "Calm";
            addAssistantMessage("Calm Mode activated. I'll be here to soothe and comfort you. 🌊");
            return;
        } else if (msg.toLowerCase().contains("switch to cbt coach mode")) {
            currentMode = "CBT";
            addAssistantMessage("CBT Coach Mode activated. Let's work through your thoughts together step-by-step. 🧠");
            return;
        } else if (msg.toLowerCase().contains("switch to listener mode")) {
            currentMode = "Listener";
            addAssistantMessage("Listener Mode activated. I'm all ears, mawa. No advice, just here for you. 👂");
            return;
        } else if (msg.toLowerCase().contains("switch to general mode")) {
            currentMode = "General";
            addAssistantMessage("Back to General Mode. How can I support you? ✨");
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String response = generateTherapeuticResponse(msg, selectedLanguage);
            addAssistantMessage(response);
        }, 1500);
    }

    private String getGreeting(String lang) {
        switch (lang) {
            case "Telugu": return "నమస్కారం మవా! నేను మైండ్‌గార్డ్ AIని. నీకు ఎలా సహాయపడగలను? ఈ రోజు ఎలా ఉంది?";
            case "Hindi": return "नमस्ते दोस्त! मैं माइंडगार्ड एआई हूँ। मैं आपकी कैसे मदद कर सकता हूँ? आज का दिन कैसा रहा?";
            default: return "Hello! I'm MindGuard AI, your supportive companion. How are you feeling today? 💙";
        }
    }

    private String generateTherapeuticResponse(String userMsg, String lang) {
        userMsg = userMsg.toLowerCase();

        // Risk Detection (Global)
        if (userMsg.contains("die") || userMsg.contains("hopeless") || userMsg.contains("worthless") || 
            userMsg.contains("chavali") || userMsg.contains("end my life") || userMsg.contains("disappear")) {
            if (lang.equals("Telugu")) return "మవా, నువ్వు అలా మాట్లాడుతుంటే నాకు చాలా బాధగా ఉంది. నీ ప్రాణం చాలా విలువైనది. దయచేసి నీకు దగ్గరగా ఉన్న వారితో లేదా ఒక డాక్టర్‌తో మాట్లాడు. నేను నీకు తోడుగా ఉంటాను, కానీ ప్రొఫెషనల్ హెల్ప్ తీసుకోవడం చాలా ముఖ్యం. 💙";
            return "I hear how much pain you're in, and it's okay to feel overwhelmed, but please know you're not alone. Your life is valuable. I strongly encourage you to reach out to a trusted person or a professional counselor right now. I'm here to support you through this. 💙";
        }

        // Mode Specific Logic
        if (currentMode.equals("Listener")) {
            if (lang.equals("Telugu")) return "అవునా మవా.. వింటున్నాను. ఇంకా చెప్పు, నీ మనసులో ఏముందో అంతా ఖాళీ చెయ్యి.";
            return "I hear you. That sounds like a lot to carry. I'm just here to listen. Go on.";
        }

        if (currentMode.equals("Calm")) {
            if (lang.equals("Telugu")) return "ప్రశాంతంగా ఉండు మవా. ఒక్క నిమిషం కళ్లు మూసుకుని గాలి పీల్చుకో. నేను నీ పక్కనే ఉన్నాను. 🌊";
            return "Just breathe slowly with me. Focus on the comfort of this moment. You are safe. 🌊";
        }

        if (currentMode.equals("CBT")) {
            return "Let's look at this. \nSituation: " + userMsg + "\nThought: What's the main thought here?\nEmotion: How does it make you feel?\nCan we find evidence for or against this thought? 🧠";
        }

        // Anxiety Support (Breathing/Grounding)
        if (userMsg.contains("panic") || userMsg.contains("anxious") || userMsg.contains("stress") || 
            userMsg.contains("tension") || userMsg.contains("భయం") || userMsg.contains("టెన్షన్")) {
            if (lang.equals("Telugu")) return "మవా, కంగారు పడకు. మనం 5-4-3-2-1 గ్రౌండింగ్ చేద్దామా? నీ చుట్టూ ఉన్న 5 వస్తువులని చూడు. మెల్లగా గాలి పీల్చుకో. 🌿";
            return "I can feel your anxiety. Let's try a quick grounding exercise. Name 5 things you can see around you right now. Keep your breaths slow and steady. 🌿";
        }

        // Language Specific General Logic (Mawa style)
        if (lang.equals("Telugu")) {
            if (userMsg.contains("బాధ") || userMsg.contains("కష్టం") || userMsg.contains("sad")) {
                return "ఫీల్ అవ్వకు మవా. ఒక్కోసారి ఇలాగే ఉంటుంది. నేను ఉన్నాను కదా! అసలు ఏమైంది? 💙";
            }
            if (userMsg.contains("హాయ్") || userMsg.contains("hi") || userMsg.contains("hello")) {
                return "హాయ్ మవా! ఎలా ఉన్నావ్? ఈరోజు విశేషాలు ఏంటి? 😊";
            }
            return "అర్థం చేసుకున్నాను మవా. ఇంకా చెప్పు.. నీకు ఏమనిపిస్తుంది? ✨";
        }

        // Default English (Supportive Sibling Style)
        if (userMsg.contains("sad") || userMsg.contains("bad")) {
            return "I'm so sorry you're feeling down. It's totally okay to feel this way. Want to tell your 'big sibling' AI what happened? 💙";
        } else if (userMsg.contains("happy") || userMsg.contains("good")) {
            return "That's amazing! I'm so happy for you, mawa! What's making today so special? 😊";
        }

        return "I hear you. Processing these thoughts is a brave step. I'm right here with you. What's on your mind? ✨";
    }

    private void addUserMessage(String text) {
        View userMsgView = getLayoutInflater().inflate(R.layout.item_chat_user, chatMessagesContainer, false);
        ((TextView) userMsgView.findViewById(R.id.txtMessage)).setText(text);
        ((TextView) userMsgView.findViewById(R.id.txtTime)).setText(getCurrentTime());
        chatMessagesContainer.addView(userMsgView);
        scrollToBottom();
    }

    private void addAssistantMessage(String text) {
        View assistantMsgView = getLayoutInflater().inflate(R.layout.item_chat_assistant, chatMessagesContainer, false);
        ((TextView) assistantMsgView.findViewById(R.id.txtMessage)).setText(text);
        ((TextView) assistantMsgView.findViewById(R.id.txtTime)).setText(getCurrentTime());
        chatMessagesContainer.addView(assistantMsgView);
        scrollToBottom();
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
    }

    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
    }
}