package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.card.MaterialCardView;

public class MoodSelectionFragment extends Fragment {

    private MaterialCardView selectedMoodCard = null;
    private int selectedImageResId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_selection, container, false);

        int[] moodCardIds = {
                R.id.cardGreat, R.id.cardGood, R.id.cardOkay, R.id.cardLow,
                R.id.cardSad, R.id.cardAnxious, R.id.cardFrustrated, R.id.cardTired
        };

        int[] moodImages = {
                R.drawable.img_28, R.drawable.img_29, R.drawable.img_30, R.drawable.img_31,
                R.drawable.img_32, R.drawable.img_33, R.drawable.img_34, R.drawable.img_35
        };

        for (int i = 0; i < moodCardIds.length; i++) {
            final int index = i;
            MaterialCardView card = view.findViewById(moodCardIds[i]);
            if (card != null) {
                card.setOnClickListener(v -> {
                    if (selectedMoodCard != null) {
                        selectedMoodCard.setStrokeWidth(0);
                    }
                    selectedMoodCard = card;
                    selectedImageResId = moodImages[index];
                    selectedMoodCard.setStrokeColor(getResources().getColor(R.color.button_blue));
                    selectedMoodCard.setStrokeWidth(4);

                    view.findViewById(R.id.btnNext).setBackgroundTintList(
                            android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.button_blue))
                    );
                });
            }
        }

        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            if (selectedMoodCard != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("moodImage", selectedImageResId);
                Navigation.findNavController(view).navigate(R.id.action_moodSelectionFragment_to_moodIntensityFragment, bundle);
            }
        });

        // Bottom Navigation Logic
        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_moodSelectionFragment_to_homeFragment);
            });
        }

        return view;
    }
}