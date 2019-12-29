package com.ygaps.travelapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ygaps.travelapp.APIConnect.APIService;
import com.ygaps.travelapp.APIConnect.InivationResponse;
import com.ygaps.travelapp.APIConnect.ResponseBody;
import com.ygaps.travelapp.APIConnect.RetrofitClient;
import com.ygaps.travelapp.APIConnect.Tour;
import com.ygaps.travelapp.APIConnect.TourInvation;
import com.ygaps.travelapp.Adapter.InvationAdapter;
import com.ygaps.travelapp.Adapter.MyAdapter;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationActivity extends AppCompatActivity {

    public static final String ACCEPT_ACTION = "Accept";
    public static final String REJECT_ACTION = "Reject";
    public static final String SHOW_ACTION = "Show";

    private Intent intent;
    private TextView tvMissionContent;
    private TextView tvMissionStatus;
    private Button btnAccept;
    private Button btnReject;
    private LinearLayout layoutAction;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_Notifications);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_ListTour:
                        String Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                        intent.putExtra("token",Token);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_History:
                        final int userId=getIntent().getIntExtra("userId",0);
                        String Token6 = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent6 = new Intent(NotificationActivity.this, HistoryActivity.class);
                        intent6.putExtra("token",Token6);
                        intent6.putExtra("userId",userId);
                        startActivity(intent6);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_Explore:
                        String Token2 = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent2 = new Intent(NotificationActivity.this, ExploreActivity.class);
                        intent2.putExtra("token",Token2);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_Notifications:
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_Setting:
                        final int userId3 = getIntent().getIntExtra("userId",0);
                        String Token4 = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent4= new Intent(NotificationActivity.this, ActivitySetting.class);
                        intent4.putExtra("token",Token4);
                        intent4.putExtra("userId",userId3);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });
        SharedPreferences shared = getSharedPreferences("tokenUser",MODE_PRIVATE);
        token = (shared.getString("token", ""));
        Log.e("FCMtokenNoti", token);
        intent = getIntent();
        try{
        process();
       }catch (Exception e) {

        }
        showMission();
    }



    private void process() {

        String action="kkk";
        try {
            action = intent.getAction();
            Log.e("matday", action);

        }catch (Exception e) {
            Log.e("matday1",action);
            return;
        }
        switch (action) {


            case ACCEPT_ACTION:
                acceptMission();
                break;
            case SHOW_ACTION:
                showMission();
                break;
            case REJECT_ACTION:
                rejectMission();
                break;
            default:
                finish();
                break;
        }
    }


    private void rejectMission() {
        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.ResponseInivation(token,intent.getStringExtra("id"),false).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showMission();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void showMission() {



        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getInvation(token,1,100).enqueue(new Callback<InivationResponse>() {
            @Override
            public void onResponse(Call<InivationResponse> call, Response<InivationResponse> response) {
                final ArrayList<TourInvation> list= (ArrayList<TourInvation>) response.body().getTours();
                RecyclerView listView=(RecyclerView) findViewById(R.id.rvNotification);
                InvationAdapter adapter=new InvationAdapter(list,NotificationActivity.this);
                listView.setAdapter(adapter);
                listView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
            }

            @Override
            public void onFailure(Call<InivationResponse> call, Throwable t) {

            }
        });

    }

    private void acceptMission() {
        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);

        apiService.ResponseInivation(token,intent.getStringExtra("id"),true).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showMission();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
