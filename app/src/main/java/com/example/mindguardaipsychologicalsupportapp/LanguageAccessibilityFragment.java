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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language_accessibility, container, false);

        languageAutoComplete = view.findViewById(R.id.languageAutoComplete);
        btnSmall = view.findViewById(R.id.btnTextSmall);
        btnMedium = view.findViewById(R.id.btnTextMedium);
        btnLarge = view.findViewById(R.id.btnTextLarge);

        // Setup Languages
        String[] languages = {"English", "Spanish", "French", "German", "Chinese", "Hindi", "Telugu"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, languages);
        languageAutoComplete.setAdapter(adapter);

        // Load saved language
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String savedLang = prefs.getString("selected_language", "English");
        languageAutoComplete.setText(savedLang, false);

        languageAutoComplete.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedLang = (String) parent.getItemAtPosition(position);
            prefs.edit().putString("selected_language", selectedLang).apply();
        });

        // Setup Text Size Buttons
        btnSmall.setOnClickListener(v -> updateTextSizeUI(btnSmall));
        btnMedium.setOnClickListener(v -> updateTextSizeUI(btnMedium));
        btnLarge.setOnClickListener(v -> updateTextSizeUI(btnLarge));

        Button nextButton = view.findViewById(R.id.nextButtonLangAcc);
        nextButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_languageAccessibilityFragment_to_stayConnectedFragment);
        });

        TextView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        return view;
    }

    private void updateTextSizeUI(Button selectedButton) {
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