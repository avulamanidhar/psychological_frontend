package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class WhyMindguardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_why_mindguard, container, false);

        TextView title = view.findViewById(R.id.titleWhy);
        TextView subtitle = view.findViewById(R.id.subtitleWhy);
        TextView card1Title = view.findViewById(R.id.cardAITitle);
        TextView card1Desc = view.findViewById(R.id.cardAIDesc);
        TextView card2Title = view.findViewById(R.id.cardPrivacyTitle);
        TextView card2Desc = view.findViewById(R.id.cardPrivacyDesc);
        TextView card3Title = view.findViewById(R.id.cardSafeTitle);
        TextView card3Desc = view.findViewById(R.id.cardSafeDesc);

        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService apiService = com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
        apiService.getWhyMindguardConfig().enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    java.util.Map<String, Object> config = response.body();
                    if (title != null) title.setText((String) config.get("title"));
                    if (subtitle != null) subtitle.setText((String) config.get("subtitle"));
                    
                    if (config.get("feature1") instanceof java.util.Map) {
                        java.util.Map<String, Object> f1 = (java.util.Map<String, Object>) config.get("feature1");
                        if (card1Title != null) card1Title.setText((String) f1.get("title"));
                        if (card1Desc != null) card1Desc.setText((String) f1.get("description"));
                    }
                    if (config.get("feature2") instanceof java.util.Map) {
                        java.util.Map<String, Object> f2 = (java.util.Map<String, Object>) config.get("feature2");
                        if (card2Title != null) card2Title.setText((String) f2.get("title"));
                        if (card2Desc != null) card2Desc.setText((String) f2.get("description"));
                    }
                    if (config.get("feature3") instanceof java.util.Map) {
                        java.util.Map<String, Object> f3 = (java.util.Map<String, Object>) config.get("feature3");
                        if (card3Title != null) card3Title.setText((String) f3.get("title"));
                        if (card3Desc != null) card3Desc.setText((String) f3.get("description"));
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                // Keep default layout text on failure
            }
        });

        Button nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_whyMindguardFragment_to_howItWorksFragment);
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