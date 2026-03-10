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
                @Override
                public void onClick(View v) {
                    if (isAvatarSelected && selectedAvatarCard != null) {
                        String avatarName = "avatar_" + avatarGrid.indexOfChild(selectedAvatarCard);
                        saveAvatarToBackend(avatarName, view);
                    } else {
                        Toast.makeText(getContext(), "Please select an avatar and age range first", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        TextView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp();
            }
        });

        return view;
    }

    private void saveAvatarToBackend(String avatarName, View view) {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");

        java.util.Map<String, Object> profileUpdates = new java.util.HashMap<>();
        profileUpdates.put("avatar_name", avatarName);

        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .updateUserProfile(userName, profileUpdates)
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Navigation.findNavController(view).navigate(R.id.action_getToKnowYouFragment_to_howCanWeHelpFragment);
                    } else {
                        Toast.makeText(getContext(), "Failed to save avatar", Toast.LENGTH_SHORT).show();
                        // Navigate anyway for demo purposes if backend fails
                        Navigation.findNavController(view).navigate(R.id.action_getToKnowYouFragment_to_howCanWeHelpFragment);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_getToKnowYouFragment_to_howCanWeHelpFragment);
                }
            });
    }
}
