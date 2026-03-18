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

        fetchFaqs(view);

        return view;
    }

    private void fetchFaqs(View view) {
        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .getFaqs()
            .enqueue(new retrofit2.Callback<java.util.List<java.util.Map<String, Object>>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.List<java.util.Map<String, Object>>> call, retrofit2.Response<java.util.List<java.util.Map<String, Object>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateFaqUI(view, response.body());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.List<java.util.Map<String, Object>>> call, Throwable t) {}
            });
    }

    private void updateFaqUI(View view, java.util.List<java.util.Map<String, Object>> faqs) {
        if (faqs.size() >= 1) updateSlot(view, R.id.txtQ1, R.id.a1, faqs.get(0));
        if (faqs.size() >= 2) updateSlot(view, R.id.txtQ2, R.id.a2, faqs.get(1));
        if (faqs.size() >= 3) updateSlot(view, R.id.txtQ3, R.id.a3, faqs.get(2));
    }

    private void updateSlot(View view, int qId, int aId, java.util.Map<String, Object> faq) {
        TextView q = view.findViewById(qId);
        TextView a = view.findViewById(aId);
        if (q != null) q.setText((String) faq.get("question"));
        if (a != null) a.setText((String) faq.get("answer"));
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