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

    private LinearLayout datesLayout;
    private LinearLayout daysOfWeekLayout;
    private LinearLayout listContainer;
    private List<MoodEntry> allEntries;
    private int selectedOffset = 6; // 0 to 6 (6 is today)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_history, container, false);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
        
        View btnLogNew = view.findViewById(R.id.btnLogNew);
        if (btnLogNew != null) {
            btnLogNew.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.moodSelectionFragment));
        }

        listContainer = view.findViewById(R.id.listContainer);
        daysOfWeekLayout = view.findViewById(R.id.daysOfWeekLayout);
        datesLayout = view.findViewById(R.id.datesLayout);

        setupCalendarUI();

        MoodEntryStorage.getAll(requireContext(), new MoodEntryStorage.MoodFetchCallback() {
            @Override
            public void onSuccess(List<MoodEntry> entries) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    allEntries = entries;
                    updateListForSelectedDate();
                });
            }

            @Override
            public void onError(String message) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    listContainer.removeAllViews();
                    TextView error = new TextView(requireContext());
                    error.setText("Error loading entries: " + message);
                    error.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    listContainer.addView(error);
                });
            }
        });

        // Bottom Navigation Listeners
        setupBottomNavigation(view);

        return view;
    }

    private void setupCalendarUI() {
        daysOfWeekLayout.removeAllViews();
        datesLayout.removeAllViews();
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_YEAR, -6); // Start from 6 days ago
        
        java.text.SimpleDateFormat dayFormat = new java.text.SimpleDateFormat("E", java.util.Locale.getDefault());
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("d", java.util.Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            final int index = i;
            String dayStr = dayFormat.format(cal.getTime()).substring(0, 1);
            String dateStr = dateFormat.format(cal.getTime());

            // Build Day Header
            TextView tvDay = new TextView(requireContext());
            LinearLayout.LayoutParams pDay = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tvDay.setLayoutParams(pDay);
            tvDay.setGravity(android.view.Gravity.CENTER);
            tvDay.setText(dayStr);
            tvDay.setTextColor(getResources().getColor(R.color.desc_gray));
            tvDay.setTextSize(12f);
            tvDay.setTypeface(null, android.graphics.Typeface.BOLD);
            daysOfWeekLayout.addView(tvDay);

            // Build Date Cell
            LinearLayout cellLayout = new LinearLayout(requireContext());
            LinearLayout.LayoutParams pCell = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            cellLayout.setLayoutParams(pCell);
            cellLayout.setGravity(android.view.Gravity.CENTER);
            cellLayout.setOrientation(LinearLayout.VERTICAL);

            if (i == selectedOffset) {
                com.google.android.material.card.MaterialCardView card = new com.google.android.material.card.MaterialCardView(requireContext());
                LinearLayout.LayoutParams pCard = new LinearLayout.LayoutParams((int)(40*getResources().getDisplayMetrics().density), (int)(40*getResources().getDisplayMetrics().density));
                card.setLayoutParams(pCard);
                card.setCardBackgroundColor(getResources().getColor(R.color.button_blue));
                card.setRadius(20*getResources().getDisplayMetrics().density);
                card.setCardElevation(0);
                
                TextView tvDate = new TextView(requireContext());
                tvDate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                tvDate.setGravity(android.view.Gravity.CENTER);
                tvDate.setText(dateStr);
                tvDate.setTextColor(getResources().getColor(R.color.white));
                tvDate.setTextSize(12f);
                tvDate.setTypeface(null, android.graphics.Typeface.BOLD);
                card.addView(tvDate);
                cellLayout.addView(card);
            } else {
                TextView tvDate = new TextView(requireContext());
                tvDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tvDate.setText(dateStr);
                tvDate.setTextColor(getResources().getColor(R.color.desc_gray));
                tvDate.setTextSize(12f);
                cellLayout.addView(tvDate);
            }

            View dot = new View(requireContext());
            LinearLayout.LayoutParams pDot = new LinearLayout.LayoutParams((int)(6*getResources().getDisplayMetrics().density), (int)(6*getResources().getDisplayMetrics().density));
            pDot.topMargin = (int)(6*getResources().getDisplayMetrics().density);
            dot.setLayoutParams(pDot);
            dot.setBackgroundResource(R.drawable.dot_calendar);
            dot.setVisibility(hasEntryOnDate(cal.getTime()) ? View.VISIBLE : View.INVISIBLE);
            cellLayout.addView(dot);

            cellLayout.setOnClickListener(v -> {
                selectedOffset = index;
                setupCalendarUI();
                updateListForSelectedDate();
            });

            datesLayout.addView(cellLayout);
            cal.add(java.util.Calendar.DAY_OF_YEAR, 1);
        }
    }

    private boolean hasEntryOnDate(java.util.Date date) {
        if (allEntries == null) return false;
        long targetEnd = date.getTime();
        for (MoodEntry e : allEntries) {
            if (TimeFormatUtils.isSameDay(e.timestampMillis, targetEnd)) return true;
        }
        return false;
    }

    private void updateListForSelectedDate() {
        if (allEntries == null) return;
        listContainer.removeAllViews();
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_YEAR, -6 + selectedOffset);
        long targetDateMs = cal.getTimeInMillis();

        java.util.List<MoodEntry> filtered = new java.util.ArrayList<>();
        for (MoodEntry e : allEntries) {
            if (TimeFormatUtils.isSameDay(e.timestampMillis, targetDateMs)) {
                filtered.add(e);

            }
        }

        if (filtered.isEmpty()) {
            TextView empty = new TextView(requireContext());
            empty.setText("No entries on this day.");
            empty.setTextColor(getResources().getColor(R.color.desc_gray));
            empty.setTextSize(14f);
            empty.setPadding(8, 24, 8, 0);
            listContainer.addView(empty);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (MoodEntry e : filtered) {
            View row = inflater.inflate(R.layout.item_mood_history, listContainer, false);
            bindRow(row, e);
            row.setOnClickListener(v -> {
                Bundle b = new Bundle();
                b.putString("entryId", e.id);
                Navigation.findNavController(getView()).navigate(R.id.action_moodHistoryFragment_to_entryDetailsFragment, b);
            });
            listContainer.addView(row);
        }
    }

    private void setupBottomNavigation(View view) {
        View btnNavHome = view.findViewById(R.id.btnNavHome);
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.homeFragment));
        }

        View btnNavMood = view.findViewById(R.id.btnNavMood);
        if (btnNavMood != null) {
            btnNavMood.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.moodSelectionFragment));
        }

        View btnNavChat = view.findViewById(R.id.btnNavChat);
        if (btnNavChat != null) {
            btnNavChat.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.chatFragment));
        }

        View btnNavTools = view.findViewById(R.id.btnNavTools);
        if (btnNavTools != null) {
            btnNavTools.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.toolsFragment));
        }

        View btnNavProfile = view.findViewById(R.id.btnNavProfile);
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.settingsFragment));
        }
    }

    private void bindRow(@NonNull View row, @NonNull MoodEntry e) {
        ImageView img = row.findViewById(R.id.imgMood);
        TextView name = row.findViewById(R.id.txtMoodName);
        TextView time = row.findViewById(R.id.txtTime);
        LinearProgressIndicator progress = row.findViewById(R.id.progressIntensity);
        ChipGroup chips = row.findViewById(R.id.chipGroupTriggers);

        img.setImageResource(e.moodImageResId);
        name.setText(e.moodName);
        time.setText(TimeFormatUtils.formatRelative(e));

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
