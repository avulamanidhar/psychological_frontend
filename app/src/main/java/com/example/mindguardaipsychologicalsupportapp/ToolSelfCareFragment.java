package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.card.MaterialCardView;
import java.util.HashSet;
import java.util.Set;

public class ToolSelfCareFragment extends Fragment {

    private Set<Integer> completedItems = new HashSet<>();
    private TextView txtCompletedCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_self_care, container, false);

        txtCompletedCount = view.findViewById(R.id.completedCount);

        setupItem(view, R.id.item1, R.id.check1, 1);
        setupItem(view, R.id.item2, R.id.check2, 2);
        setupItem(view, R.id.item3, R.id.check3, 3);
        setupItem(view, R.id.item4, R.id.check4, 4);
        setupItem(view, R.id.item5, R.id.check5, 5);
        setupItem(view, R.id.item6, R.id.check6, 6);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        view.findViewById(R.id.btnComplete).setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("exerciseName", "Self-Care");
            Navigation.findNavController(view).navigate(R.id.action_toolSelfCareFragment_to_toolCompleteFragment, b);
        });

        // Bottom Navigation
        view.findViewById(R.id.btnNavHome).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolSelfCareFragment_to_homeFragment));
        view.findViewById(R.id.btnNavMood).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolSelfCareFragment_to_moodSelectionFragment));
        view.findViewById(R.id.btnNavChat).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolSelfCareFragment_to_chatFragment));
        view.findViewById(R.id.btnNavTools).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolSelfCareFragment_to_toolsFragment));
        view.findViewById(R.id.btnNavProfile).setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_toolSelfCareFragment_to_settingsFragment));

        updateCount();

        return view;
    }

    private void setupItem(View root, int itemId, int checkId, int index) {
        MaterialCardView item = root.findViewById(itemId);
        ImageView check = root.findViewById(checkId);

        if (item != null) {
            item.setOnClickListener(v -> {
                if (completedItems.contains(index)) {
                    completedItems.remove(index);
                    check.setImageResource(R.drawable.option_radio_unchecked);
                } else {
                    completedItems.add(index);
                    check.setImageResource(R.drawable.ic_check); // Make sure ic_check exists
                }
                updateCount();
            });
        }
    }

    private void updateCount() {
        if (txtCompletedCount != null) {
            txtCompletedCount.setText(completedItems.size() + "/6 completed");
        }
    }
}
