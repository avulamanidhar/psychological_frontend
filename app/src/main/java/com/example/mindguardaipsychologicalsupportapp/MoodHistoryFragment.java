package com.example.mindguardaipsychologicalsupportapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Date;
import java.util.List;

public class MoodHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_history, container, false);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        view.findViewById(R.id.btnLogNew).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.moodSelectionFragment));

        LinearLayout listContainer = view.findViewById(R.id.listContainer);
        List<MoodEntry> entries = MoodEntryStorage.getAll(requireContext());

        listContainer.removeAllViews();
        if (entries.isEmpty()) {
            TextView empty = new TextView(requireContext());
            empty.setText("No entries yet. Tap + Log New to add your first mood.");
            empty.setTextColor(getResources().getColor(R.color.desc_gray));
            empty.setTextSize(14f);
            empty.setPadding(8, 24, 8, 0);
            listContainer.addView(empty);
            return view;
        }

        for (MoodEntry e : entries) {
            View row = inflater.inflate(R.layout.item_mood_history, listContainer, false);
            bindRow(row, e);
            row.setOnClickListener(v -> {
                Bundle b = new Bundle();
                b.putString("entryId", e.id);
                Navigation.findNavController(view).navigate(R.id.action_moodHistoryFragment_to_entryDetailsFragment, b);
            });
            listContainer.addView(row);
        }

        return view;
    }

    private void bindRow(@NonNull View row, @NonNull MoodEntry e) {
        ImageView img = row.findViewById(R.id.imgMood);
        TextView name = row.findViewById(R.id.txtMoodName);
        TextView time = row.findViewById(R.id.txtTime);
        LinearProgressIndicator progress = row.findViewById(R.id.progressIntensity);
        ChipGroup chips = row.findViewById(R.id.chipGroupTriggers);

        img.setImageResource(e.moodImageResId);
        name.setText(e.moodName);
        time.setText(TimeFormatUtils.formatRelative(new Date(e.timestampMillis)));

        progress.setProgress(e.intensity);
        int indicator = MoodUi.colorForMood(getResources(), e.moodName);
        progress.setIndicatorColor(indicator);
        progress.setTrackColor(getResources().getColor(R.color.bg_splash));

        chips.removeAllViews();
        for (int i = 0; i < Math.min(2, e.triggers.size()); i++) {
            Chip c = new Chip(requireContext());
            c.setText(e.triggers.get(i));
            c.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.icon_bg_blue)));
            c.setTextColor(getResources().getColor(R.color.button_blue));
            c.setChipCornerRadius(12f);
            c.setClickable(false);
            c.setCheckable(false);
            chips.addView(c);
        }
        chips.setVisibility(e.triggers.isEmpty() ? View.GONE : View.VISIBLE);
    }
}

