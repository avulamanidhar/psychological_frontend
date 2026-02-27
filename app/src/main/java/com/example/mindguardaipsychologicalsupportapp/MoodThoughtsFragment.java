package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class MoodThoughtsFragment extends Fragment {

    private int moodImageResId;
    private String moodName;
    private int intensityValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_thoughts, container, false);

        // Get passed data
        if (getArguments() != null) {
            moodImageResId = getArguments().getInt("moodImage");
            moodName = getArguments().getString("moodName");
            intensityValue = getArguments().getInt("intensity");
        }

        EditText etThoughts = view.findViewById(R.id.etThoughts);
        TextView txtCharCount = view.findViewById(R.id.txtCharCount);
        TextView txtWordCount = view.findViewById(R.id.txtWordCount);

        etThoughts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                txtCharCount.setText(text.length() + " characters");
                
                if (text.isEmpty()) {
                    txtWordCount.setText("0 words");
                } else {
                    String[] words = text.split("\\s+");
                    txtWordCount.setText(words.length + " words");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("moodImage", moodImageResId);
            bundle.putString("moodName", moodName);
            bundle.putInt("intensity", intensityValue);
            bundle.putString("thoughts", etThoughts.getText().toString());
            
            Navigation.findNavController(view).navigate(R.id.action_moodThoughtsFragment_to_moodTriggersFragment, bundle);
        });

        return view;
    }
}