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

public class HowItWorksFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how_it_works, container, false);

        Button nextButton = view.findViewById(R.id.nextButtonHow);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fixed: Updated action ID to navigate to Privacy & Consent screen
                Navigation.findNavController(view).navigate(R.id.action_howItWorksFragment_to_privacyConsentFragment);
            }
        });

        TextView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp();
            }
        });

        fetchHowItWorksSteps(view);

        return view;
    }

    private void fetchHowItWorksSteps(View view) {
        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .getHowItWorksSteps()
            .enqueue(new retrofit2.Callback<java.util.List<java.util.Map<String, Object>>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.List<java.util.Map<String, Object>>> call, retrofit2.Response<java.util.List<java.util.Map<String, Object>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateStepsUI(view, response.body());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.List<java.util.Map<String, Object>>> call, Throwable t) {}
            });
    }

    private void updateStepsUI(View view, java.util.List<java.util.Map<String, Object>> steps) {
        if (steps.size() >= 1) updateSlot(view, R.id.txtStepTitle1, R.id.txtStepDesc1, steps.get(0));
        if (steps.size() >= 2) updateSlot(view, R.id.txtStepTitle2, R.id.txtStepDesc2, steps.get(1));
        if (steps.size() >= 3) updateSlot(view, R.id.txtStepTitle3, R.id.txtStepDesc3, steps.get(2));
    }

    private void updateSlot(View view, int titleId, int descId, java.util.Map<String, Object> data) {
        TextView title = view.findViewById(titleId);
        TextView desc = view.findViewById(descId);
        if (title != null) title.setText((String) data.get("title"));
        if (desc != null) desc.setText((String) data.get("description"));
    }
}