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
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAvatarSelected) {
                    Navigation.findNavController(view).navigate(R.id.action_getToKnowYouFragment_to_whatBringsYouHereFragment);
                } else {
                    Toast.makeText(getContext(), "Please select an avatar and age range first", Toast.LENGTH_SHORT).show();
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
}