package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        Button continueButton = view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fixed: Updated action ID to match the one in nav_graph.xml
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_whyMindguardFragment);
            }
        });

        // Backend Health Check & Config Fetch
        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService apiService = 
            com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
            
        apiService.getSystemStatus().enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    android.util.Log.d("SplashFragment", "Backend online");
                    String version = (String) response.body().get("version");
                    if (version != null && getActivity() != null) {
                        android.content.SharedPreferences prefs = getActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
                        prefs.edit().putString("backend_version", version).apply();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                if (getContext() != null) {
                    android.widget.Toast.makeText(getContext(), "Connecting to local backend failed. Is server running?", android.widget.Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}