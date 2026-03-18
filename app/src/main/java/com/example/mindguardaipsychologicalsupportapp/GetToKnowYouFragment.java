package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class GetToKnowYouFragment extends Fragment {

    private boolean isAvatarSelected = false;
    private CardView selectedAvatarCard = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_to_know_you, container, false);

        // Set up Age Range Dropdown
        android.widget.AutoCompleteTextView ageRangeDropdown = view.findViewById(R.id.ageRangeDropdown);
        if (ageRangeDropdown != null) {
            String[] ageRanges = new String[]{"Under 18", "18 - 24", "25 - 34", "35 - 44", "45 - 54", "55 - 64", "65+"};
            android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    ageRanges
            );
            ageRangeDropdown.setAdapter(adapter);
        }

        ViewGroup avatarGrid = view.findViewById(R.id.avatarGrid);
        int defaultCardColor = getResources().getColor(R.color.white);
        int selectedColor = getResources().getColor(R.color.icon_bg_blue);

        for (int i = 0; i < avatarGrid.getChildCount(); i++) {
            final CardView card = (CardView) avatarGrid.getChildAt(i);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Reset previous selection
                    if (selectedAvatarCard != null) {
                        selectedAvatarCard.setCardBackgroundColor(defaultCardColor);
                    }
                    
                    // Set new selection
                    selectedAvatarCard = card;
                    selectedAvatarCard.setCardBackgroundColor(selectedColor);
                    isAvatarSelected = true;
                }
            });
        }

        Button nextButton = view.findViewById(R.id.nextButtonKnowYou);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAvatarSelected && selectedAvatarCard != null) {
                    String avatarName = "avatar_" + avatarGrid.indexOfChild(selectedAvatarCard);
                    
                    // Also get the age range from the dropdown
                    String ageRange = "";
                    com.google.android.material.textfield.TextInputLayout ageLayout = view.findViewById(R.id.ageRangeLayout);
                    if (ageLayout != null && ageLayout.getEditText() != null) {
                        ageRange = ageLayout.getEditText().getText().toString();
                    }

                    saveAvatarAndAgeToBackend(avatarName, ageRange, view);
                } else {
                    Toast.makeText(getContext(), "Please select an avatar first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp();
            }
        });

        return view;
    }

    private void saveAvatarAndAgeToBackend(String avatarName, String ageRange, View view) {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        java.util.Map<String, Object> profileUpdates = new java.util.HashMap<>();
        profileUpdates.put("avatar_name", avatarName);
        profileUpdates.put("age_range", ageRange);

        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService api = com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
        api.updateUserProfile(userName, profileUpdates)
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Navigation.findNavController(view).navigate(R.id.action_getToKnowYouFragment_to_howCanWeHelpFragment);
                    } else {
                        Toast.makeText(getContext(), "Failed to save profile details", Toast.LENGTH_SHORT).show();
                        // Navigate anyway for demo purposes
                        Navigation.findNavController(view).navigate(R.id.action_getToKnowYouFragment_to_howCanWeHelpFragment);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_getToKnowYouFragment_to_howCanWeHelpFragment);
                }
            });
    }
}
