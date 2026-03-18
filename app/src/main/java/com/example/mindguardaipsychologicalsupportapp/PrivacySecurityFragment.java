package com.example.mindguardaipsychologicalsupportapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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

import com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService;
import com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacySecurityFragment extends Fragment {

    private MindGuardApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy_security, container, false);


        apiService = RetrofitClient.getApiService();

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        Button btnExport = view.findViewById(R.id.btnExportData);
        if (btnExport != null) {
            btnExport.setOnClickListener(v -> handleExportData());
        }

        Button btnDelete = view.findViewById(R.id.btnDeleteData);
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> showDeleteDataConfirmation());
        }

        return view;
    }

    private void handleExportData() {
        Toast.makeText(getContext(), "Preparing your data export...", Toast.LENGTH_SHORT).show();
        apiService.exportUserData().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (isAdded()) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String dataStr = json.toString(4);
                            
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "MindGuard AI Data Export:\n\n" + dataStr);
                            sendIntent.setType("text/plain");
                            
                            Intent shareIntent = Intent.createChooser(sendIntent, "Export My Data");
                            startActivity(shareIntent);
                            
                        } catch (Exception e) {
                            Toast.makeText(requireContext(), "Export error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to export data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDeleteDataConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete All My Data?")
                .setMessage("This action is permanent and cannot be undone. All your mood entries, chat history, and activities will be deleted from our servers.")
                .setPositiveButton("Delete Everything", (dialog, which) -> deleteUserData())
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteUserData() {
        apiService.deleteUserData().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (isAdded()) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "All your personal data has been deleted.", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(requireView()).navigate(R.id.action_privacySecurityFragment_to_homeFragment);
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
