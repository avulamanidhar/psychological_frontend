package com.example.mindguardaipsychologicalsupportapp.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.213.230.185:8000/";
    private static Retrofit retrofit = null;
    private static final int MAX_RETRIES = 3;

    public static MindGuardApiService getApiService() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d("RetrofitLog", message));
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new NetworkConnectionInterceptor(com.example.mindguardaipsychologicalsupportapp.MindGuardApplication.getInstance().getApplicationContext()))
                    .addInterceptor(pathRetryInterceptor())
                    .retryOnConnectionFailure(true)
                    .connectTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(MindGuardApiService.class);
    }

    public static MindGuardApiService getApiService(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d("RetrofitAuthLog", message));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new NetworkConnectionInterceptor(context))
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
                    String token = prefs.getString("auth_token", "");

                    Request.Builder requestBuilder = original.newBuilder();
                    String path = original.url().encodedPath();
                    boolean isPublicEndpoint = path.contains("/login") || 
                                             path.contains("/users/") || 
                                             path.contains("/forgot-password") || 
                                             path.contains("/verify-otp") || 
                                             path.contains("/reset-password");

                    if (token != null && !token.isEmpty() && !isPublicEndpoint) {
                        requestBuilder.header("Authorization", "Bearer " + token);
                    }

                    int retryCount = 0;
                    Response response = null;
                    Exception lastException = null;

                    while (retryCount < MAX_RETRIES) {
                        try {
                            if (response != null) response.close();
                            response = chain.proceed(requestBuilder.build());
                            if (response.isSuccessful() || response.code() < 500) {
                                return response;
                            }
                        } catch (Exception e) {
                            lastException = e;
                            Log.e("RetrofitAuthError", "Attempt " + (retryCount + 1) + " failed: " + e.getMessage());
                        }
                        retryCount++;
                        try { Thread.sleep(1000 * retryCount); } catch (InterruptedException ignored) {}
                    }

                    if (response != null) return response;
                    if (lastException instanceof IOException) throw (IOException) lastException;
                    throw new IOException("Failed after " + MAX_RETRIES + " retries", lastException);
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        Retrofit authRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return authRetrofit.create(MindGuardApiService.class);
    }

    private static Interceptor pathRetryInterceptor() {
        return chain -> {
            int retryCount = 0;
            Response response = null;
            Exception lastException = null;

            while (retryCount < MAX_RETRIES) {
                try {
                    if (response != null) response.close();
                    response = chain.proceed(chain.request());
                    if (response.isSuccessful() || response.code() < 500) {
                        return response;
                    }
                } catch (Exception e) {
                    lastException = e;
                    Log.e("RetrofitError", "Attempt " + (retryCount + 1) + " failed: " + e.getMessage());
                }
                retryCount++;
                try { Thread.sleep(1000 * retryCount); } catch (InterruptedException ignored) {}
            }

            if (response != null) return response;
            if (lastException instanceof IOException) throw (IOException) lastException;
            throw new IOException("Failed after " + MAX_RETRIES + " retries", lastException);
        };
    }
}
