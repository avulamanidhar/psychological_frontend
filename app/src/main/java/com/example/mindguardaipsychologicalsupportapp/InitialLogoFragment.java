package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class InitialLogoFragment extends Fragment {

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable navigateRunnable = () -> navigateToSplash();
    private boolean navigated = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_logo, container, false);

        // Touch to navigate
        view.findViewById(R.id.rootLayout).setOnClickListener(v -> navigateToSplash());

        // Warm up the server while showing logo
        warmupServer();

        // Auto navigate after 5 seconds
        handler.postDelayed(navigateRunnable, 5000);

        return view;
    }

    private void warmupServer() {
        com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService()
            .getSystemStatus()
            .enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                    // Log or handle server readiness if needed
                    android.util.Log.d("InitialLogo", "Server status: " + (response.isSuccessful() ? "online" : "error"));
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                    android.util.Log.e("InitialLogo", "Server connection failed", t);
                }
            });
    }

    private void navigateToSplash() {
        if (!navigated) {
            navigated = true;
            handler.removeCallbacks(navigateRunnable);
            Navigation.findNavController(requireView()).navigate(R.id.action_initialLogoFragment_to_splashFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(navigateRunnable);
    }
}