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

public class HelpFaqFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_faq, container, false);
        
        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        setupExpandable(view, R.id.q1, R.id.a1, R.id.ic1);
        setupExpandable(view, R.id.q2, R.id.a2, R.id.ic2);
        setupExpandable(view, R.id.q3, R.id.a3, R.id.ic3);

        return view;
    }

    private void setupExpandable(View root, int questionId, int answerId, int iconId) {
        View question = root.findViewById(questionId);
        TextView answer = root.findViewById(answerId);
        ImageView icon = root.findViewById(iconId);

        if (question != null && answer != null) {
            question.setOnClickListener(v -> {
                if (answer.getVisibility() == View.GONE) {
                    answer.setVisibility(View.VISIBLE);
                    icon.setRotation(180);
                } else {
                    answer.setVisibility(View.GONE);
                    icon.setRotation(0);
                }
            });
        }
    }
}