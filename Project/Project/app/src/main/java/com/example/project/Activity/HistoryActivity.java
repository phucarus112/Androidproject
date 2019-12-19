package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.ListToursHistoryResponse;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.StatusResponse;
import com.example.project.APIConnect.TourHistory;
import com.example.project.APIConnect.UpdateTourResponse;
import com.example.project.Adapter.MyAdapterHistory;
import com.example.project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView listView;
    SearchView editsearch;
    TextView countTour,countCancelled,countOpem,countStarted,countClose;
    MyAdapterHistory adapter;
    String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));

        editsearch = (SearchView) findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(this);

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_history);
        bottomNavigationView.setSelectedItemId(R.id.navigation_History);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_ListTour:
                        Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                        intent.putExtra("token",Token);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_History:
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_Explore:
                        Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent2 = new Intent(HistoryActivity.this, ExploreActivity.class);
                        intent2.putExtra("token",Token);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_Notifications:
                        //Toast.makeText(com.example.project.Activity.MainActivity.this, "notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_Setting:
                       // Toast.makeText(com.example.project.Activity.MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        //luu token user vao shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("tokenUser",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        Token = getIntent().getStringExtra("token");
        editor.putString("token",Token);
        editor.commit();

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar, null);
        actionBar.setCustomView(v);
        Intent i = getIntent();
        final String Token = i.getStringExtra("token");
        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);

        apiService.getResponseListTourHistory(Token,1,100).enqueue(new Callback<ListToursHistoryResponse>() {
            @Override
            public void onResponse(Call<ListToursHistoryResponse> call, Response<ListToursHistoryResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<TourHistory> list = (ArrayList<TourHistory>) response.body().getTours();
                    Collections.reverse(list);

                    SharedPreferences cancel = getSharedPreferences("saveSP",MODE_PRIVATE);

                    String[] tokenList = cancel.getString("tourCancel","").split("/");
                    Log.e("tokenlist", cancel.getString("tourCancel",""));
                    SharedPreferences.Editor cancelEditor = cancel.edit();
                    cancelEditor.putString("tourCancel","");
                    cancelEditor.commit();

                    for(int i=0;i<tokenList.length;i++)
                    {
                        for(int j=0;j<list.size();j++)
                        {
                            if(tokenList[i].equals(String.valueOf(list.get(j).getId())) && !tokenList[i].equals("null"))
                            {
                                list.get(j).setStatus(-1);
                                //tien hanh cap nhat
                                Retrofit retrofit = RetrofitClient.getClient();
                                APIService apiService = retrofit.create(APIService.class);
                                apiService.updateTour(Token, list.get(j).getId(),
                                        list.get(j).getName(), Long.parseLong(list.get(j).getStartDate()),
                                        Long.parseLong(list.get(j).getEndDate()),
                                       list.get(j).getAdults(),list.get(j).getChilds(),
                                        Integer.parseInt(list.get(j).getMinCost()),
                                        Integer.parseInt(list.get(j).getMaxCost()),-1)
                                        .enqueue(new Callback<UpdateTourResponse>() {
                                            @Override
                                            public void onResponse(Call<UpdateTourResponse> call, Response<UpdateTourResponse> response) {
                                                if(response.isSuccessful())
                                                {

                                                }
                                                else
                                                {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                        Toast.makeText(HistoryActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<UpdateTourResponse> call, Throwable t) {
                                                Toast.makeText(HistoryActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }

                    for(int i=0;i<list.size();i++)
                    {
                        if(list.get(i).getStatus()==-1)
                        {
                            list.remove(i);
                            i--;
                        }
                    }

                    listView = (RecyclerView) findViewById(R.id.rvTours_history);
                    adapter = new MyAdapterHistory(list,HistoryActivity.this);
                    listView.setAdapter(adapter);
                    listView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    countTour = (TextView) findViewById(R.id.countTour_history);
                    countTour.setText(response.body().getTotal().toString() + " trips");

                    Retrofit retrofit2 = RetrofitClient.getClient();
                    APIService apiService2 = retrofit2.create(APIService.class);
                    apiService2.getStatusTotal(Token)
                            .enqueue(new Callback<StatusResponse>() {
                                @Override
                                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                    if(response.isSuccessful())
                                    {
                                        ArrayList<StatusResponse.Status> statusResponses = response.body().getListStatus();
                                        countCancelled = (TextView) findViewById(R.id.tv_countCancelled);
                                        countOpem = (TextView) findViewById(R.id.tv_countOpen);
                                        countStarted = (TextView) findViewById(R.id.tv_countStarted);
                                        countClose = (TextView) findViewById(R.id.tv_countClose);
                                        countCancelled.setText(String.valueOf(statusResponses.get(0).getTotal()));
                                        countOpem.setText(String.valueOf(statusResponses.get(1).getTotal()));
                                        countStarted.setText(String.valueOf(statusResponses.get(2).getTotal()));
                                        countClose.setText(String.valueOf(statusResponses.get(3).getTotal()));
                                    }
                                    else
                                    {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<StatusResponse> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<ListToursHistoryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.getFilter().filter(newText);
        return false;
    }
}
