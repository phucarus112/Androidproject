package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.ListToursResponse;
import com.example.project.APIConnect.LoginRequest;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.Tour;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    RecyclerView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent i = getIntent();
        final String Token = i.getStringExtra("token");

        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getResponseListTour(Token,193,1).enqueue(new Callback<ListToursResponse>() {
            @Override
            public void onResponse(Call<ListToursResponse> call, Response<ListToursResponse> response) {
                if(response.isSuccessful())
                {
                    ArrayList<Tour> list= (ArrayList<Tour>) response.body().getTours();
                    listView=(RecyclerView) findViewById(R.id.rvTours);
                    MyAdapter adapter=new MyAdapter(list);
                    listView.setAdapter(adapter);
                    listView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    Toast.makeText(MainActivity.this, Token, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListToursResponse> call, Throwable t) {

            }
        });
    }
}
