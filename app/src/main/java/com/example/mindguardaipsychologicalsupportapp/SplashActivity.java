package com.example.mindguardaipsychologicalsupportapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Backend Health Check & Config Fetch
        com.example.mindguardaipsychologicalsupportapp.api.MindGuardApiService apiService = 
            com.example.mindguardaipsychologicalsupportapp.api.RetrofitClient.getApiService();
            
        apiService.getSystemStatus().enqueue(new retrofit2.Callback<java.util.Map<String, Object>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, Object>> call, retrofit2.Response<java.util.Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    android.util.Log.d("SplashActivity", "Backend online");
                    String version = (String) response.body().get("version");
                    if (version != null) {
                        android.content.SharedPreferences prefs = getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE);
                        prefs.edit().putString("backend_version", version).apply();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, Object>> call, Throwable t) {
                android.widget.Toast.makeText(SplashActivity.this, "Connecting to local backend failed. Is server running?", android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }
}