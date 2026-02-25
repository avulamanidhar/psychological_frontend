package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.card.MaterialCardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import java.util.HashSet;
import java.util.Set;

public class WhatBringsYouHereFragment extends Fragment {

    private Set<Integer> selectedItems = new HashSet<>();
    private Button continueButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_what_brings_you_here, container, false);

        continueButton = view.findViewById(R.id.continueButtonWhatBrings);
        
        int[] cardIds = {
            R.id.cardManageStress, R.id.cardReduceAnxiety, 
            R.id.cardBetterFocus, R.id.cardImproveSleep, 
            R.id.cardBoostMood, R.id.cardBuildResilience
        };

        for (int id : cardIds) {
            MaterialCardView card = view.findViewById(id);
            card.setOnClickListener(v -> toggleSelection(card, id));
        }

        continueButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_whatBringsYouHereFragment_to_howCanWeHelpFragment);
        });

        return view;
    }

    private void toggleSelection(MaterialCardView card, int id) {
        if (selectedItems.contains(id)) {
            selectedItems.remove(id);
            card.setCardBackgroundColor(getResources().getColor(R.color.white));
            card.setStrokeColor(ColorStateList.valueOf(getResources().getColor(android.R.color.transparent)));
            card.setStrokeWidth(0);
        } else {
            selectedItems.add(id);
            card.setCardBackgroundColor(getResources().getColor(R.color.icon_bg_blue));
            card.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));
            card.setStrokeWidth(4);
        }
        updateContinueButton();
    }

    private void updateContinueButton() {
        int count = selectedItems.size();
        continueButton.setText("Continue (" + count + " selected)");
        
        if (count > 0) {
            continueButton.setEnabled(true);
            continueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_blue)));
            continueButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            continueButton.setEnabled(false);
            continueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
            continueButton.setTextColor(getResources().getColor(R.color.desc_gray));
        }
    }
}