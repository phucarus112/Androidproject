package com.example.project.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.LoginRequest;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        SharedPreferences sharedPreferences2= getSharedPreferences("tourSP",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.putString("isCompleted","no");
        editor2.commit();
        SharedPreferences sharedPreferences3 = getSharedPreferences("saveSP",MODE_PRIVATE);
        SharedPreferences.Editor editor3 = sharedPreferences3.edit();
        editor3.putString("have","no");
        editor3.putString("hoan thanh","not ok");
        editor3.putString("tim sau khi tao","no");
        editor3.commit();

        SharedPreferences sharedPreferences = sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String token = sharedPreferences.getString("isLogined", "");
        if (token.equals("yes")) {

            if(sharedPreferences.getString("isLoginedFB","").equals("yes"))
            {
                Intent change = new Intent(SplashActivity.this, MainActivity.class);
                change.putExtra("token", sharedPreferences.getString("tokenFB",""));
                Log.e("token khi log in",sharedPreferences.getString("tokenFB",""));
                SharedPreferences sharedPreferences5 = getSharedPreferences("tokenUser",MODE_PRIVATE);
                SharedPreferences.Editor editor5 = sharedPreferences5.edit();
                editor5.putString("token",sharedPreferences.getString("tokenFB",""));
                editor5.commit();
                startActivity(change);
                finish();
            }
            else {
                Retrofit retrofit = RetrofitClient.getClient();
                APIService apiService = retrofit.create(APIService.class);
                apiService.login(sharedPreferences.getString("emailPhone", ""),
                        sharedPreferences.getString("password", ""))
                        .enqueue(new Callback<LoginRequest>() {
                            @Override
                            public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
                                Intent change = new Intent(SplashActivity.this, MainActivity.class);
                                change.putExtra("token", response.body().getToken());
                                Log.e("token khi log in", response.body().getToken());
                                SharedPreferences sharedPreferences2 = getSharedPreferences("tokenUser", MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                editor2.putString("userId",String.valueOf(response.body().getUserId()));
                                editor2.putString("token", response.body().getToken());
                                editor2.commit();
                                startActivity(change);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<LoginRequest> call, Throwable t) {
                                Toast.makeText(SplashActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        else
        {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        }
    }
}
