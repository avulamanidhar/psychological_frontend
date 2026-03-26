package com.example.mindguardaipsychologicalsupportapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mindguardaipsychologicalsupportapp.utils.NetworkUtils;

public class NetworkErrorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network_error, container, false);

        Button btnRetry = view.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(v -> {
            if (NetworkUtils.isInternetAvailable(requireContext())) {
                Navigation.findNavController(view).navigate(R.id.action_networkErrorFragment_to_splashFragment);
            } else {
                Toast.makeText(getContext(), "Still no connection. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
