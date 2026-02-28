package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Date;
import java.util.Locale;

public class EntryDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_details, container, false);

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        }

        String entryId = getArguments() == null ? null : getArguments().getString("entryId");
        MoodEntry entry = entryId == null ? null : MoodEntryStorage.getById(requireContext(), entryId);
        if (entry == null) {
            Navigation.findNavController(view).navigateUp();
            return view;
        }

        bind(view, entry);
        return view;
    }

    private void bind(@NonNull View view, @NonNull MoodEntry e) {
        TextView txtDate = view.findViewById(R.id.txtDate);
        MaterialCardView cardEmoji = view.findViewById(R.id.cardEmoji);
        ImageView imgMood = view.findViewById(R.id.imgMood);
        TextView moodName = view.findViewById(R.id.txtMoodName);
        TextView intensity = view.findViewById(R.id.txtIntensity);

        View sectionTriggers = view.findViewById(R.id.sectionTriggers);
        ChipGroup chipGroupTriggers = view.findViewById(R.id.chipGroupTriggers);
        TextView labelJournal = view.findViewById(R.id.labelJournal);
        TextView txtJournal = view.findViewById(R.id.txtJournal);
        TextView txtAi = view.findViewById(R.id.txtAiReflection);

        if (txtDate != null) txtDate.setText(TimeFormatUtils.formatRelative(new Date(e.timestampMillis)));
        if (imgMood != null) imgMood.setImageResource(e.moodImageResId);
        if (moodName != null) moodName.setText(e.moodName);
        if (intensity != null) intensity.setText(String.format(Locale.getDefault(), "Intensity: %d%%", e.intensity));

        if (cardEmoji != null) {
            cardEmoji.setCardBackgroundColor(MoodUi.cardBgForMood(e.moodName));
        }

        if (chipGroupTriggers != null && sectionTriggers != null) {
            chipGroupTriggers.removeAllViews();
            if (e.triggers == null || e.triggers.isEmpty()) {
                sectionTriggers.setVisibility(View.GONE);
                chipGroupTriggers.setVisibility(View.GONE);
            } else {
                sectionTriggers.setVisibility(View.VISIBLE);
                chipGroupTriggers.setVisibility(View.VISIBLE);
                for (String t : e.triggers) {
                    Chip c = new Chip(requireContext());
                    c.setText(t);
                    c.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.icon_bg_blue)));
                    c.setTextColor(ContextCompat.getColor(requireContext(), R.color.button_blue));
                    c.setChipCornerRadius(12f);
                    c.setClickable(false);
                    c.setCheckable(false);
                    chipGroupTriggers.addView(c);
                }
            }
        }

        String journal = e.journal == null ? "" : e.journal.trim();
        if (labelJournal != null && txtJournal != null) {
            if (journal.isEmpty()) {
                labelJournal.setVisibility(View.GONE);
                txtJournal.setVisibility(View.GONE);
            } else {
                labelJournal.setVisibility(View.VISIBLE);
                txtJournal.setVisibility(View.VISIBLE);
                txtJournal.setText(String.format(Locale.getDefault(), "“%s”", journal));
            }
        }

        if (txtAi != null) txtAi.setText(e.aiReflection);
    }
}
