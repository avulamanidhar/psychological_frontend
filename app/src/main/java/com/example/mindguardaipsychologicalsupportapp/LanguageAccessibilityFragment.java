package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LanguageAccessibilityFragment extends Fragment {

    private Button btnSmall, btnMedium, btnLarge;
    private AutoCompleteTextView languageAutoComplete;
    private androidx.appcompat.widget.SwitchCompat switchContrast, switchReader;
    private String selectedTextSize = "Medium";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language_accessibility, container, false);

        languageAutoComplete = view.findViewById(R.id.languageAutoComplete);
        btnSmall = view.findViewById(R.id.btnTextSmall);
        btnMedium = view.findViewById(R.id.btnTextMedium);
        btnLarge = view.findViewById(R.id.btnTextLarge);
        switchContrast = view.findViewById(R.id.switchHighContrast);
        switchReader = view.findViewById(R.id.switchScreenReader);

        // Setup Languages
        String[] languages = {"English", "Spanish", "French", "German", "Chinese", "Hindi", "Telugu"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, languages);
        languageAutoComplete.setAdapter(adapter);

        // Load saved preferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        languageAutoComplete.setText(prefs.getString("selected_language", "English"), false);
        selectedTextSize = prefs.getString("selected_text_size", "Medium");
        
        // Initial UI state
        if ("Small".equals(selectedTextSize)) updateTextSizeUI(btnSmall, "Small");
        else if ("Large".equals(selectedTextSize)) updateTextSizeUI(btnLarge, "Large");
        else updateTextSizeUI(btnMedium, "Medium");

        if (switchContrast != null) switchContrast.setChecked(prefs.getBoolean("high_contrast", false));
        if (switchReader != null) switchReader.setChecked(prefs.getBoolean("screen_reader", false));

        // Setup Listeners
        btnSmall.setOnClickListener(v -> updateTextSizeUI(btnSmall, "Small"));
        btnMedium.setOnClickListener(v -> updateTextSizeUI(btnMedium, "Medium"));
        btnLarge.setOnClickListener(v -> updateTextSizeUI(btnLarge, "Large"));

        Button nextButton = view.findViewById(R.id.nextButtonLangAcc);
        nextButton.setOnClickListener(v -> saveSettingsToBackend(view));

        TextView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        return view;
    }

    private void saveSettingsToBackend(View view) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");
        String selectedLang = languageAutoComplete.getText().toString();
        boolean contrast = switchContrast != null && switchContrast.isChecked();
        boolean reader = switchReader != null && switchReader.isChecked();

        // Save locally
        prefs.edit()
            .putString("selected_language", selectedLang)
            .putString("selected_text_size", selectedTextSize)
            .putBoolean("high_contrast", contrast)
            .putBoolean("screen_reader", reader)
            .apply();

        java.util.Map<String, Object> updates = new java.util.HashMap<>();
        updates.put("language", selectedLang);
        updates.put("text_size", selectedTextSize);
        updates.put("high_contrast", contrast);
        updates.put("screen_reader", reader);

        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .updateUserProfile(userName, updates)
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    Navigation.findNavController(view).navigate(R.id.action_languageAccessibilityFragment_to_stayConnectedFragment);
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    Navigation.findNavController(view).navigate(R.id.action_languageAccessibilityFragment_to_stayConnectedFragment);
                }
            });
    }

    private void updateTextSizeUI(Button selectedButton, String size) {
        selectedTextSize = size;
        resetButtonStyle(btnSmall);
        resetButtonStyle(btnMedium);
        resetButtonStyle(btnLarge);

        selectedButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.button_blue)));
        selectedButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
    }

    private void resetButtonStyle(Button button) {
        button.setBackgroundTintList(ColorStateList.valueOf(android.graphics.Color.TRANSPARENT));
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.desc_gray));
    }
}