package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.ListToursResponse;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.Tour;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    RecyclerView listView;
    SearchView editsearch;
    TextView countTour;
    MyAdapter adapter;
    SharedPreferences sharedPreferences;
    Button btnCreate;
    ActionBar toolbar;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreate =(Button) findViewById(R.id.btn_create);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        sharedPreferences2= getSharedPreferences("data",MODE_PRIVATE);
        editor=  sharedPreferences2.edit();
        editor.clear();
        editor.commit();

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_ListTour);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_ListTour:
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_Notifications:
                        Toast.makeText(MainActivity.this, "notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_User:
                        Toast.makeText(MainActivity.this, "user", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_Setting:
                        Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar, null);
        actionBar.setCustomView(v);
        Intent i = getIntent();
        final String Token = i.getStringExtra("token");
        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getResponseListTour(Token,515,1).enqueue(new Callback<ListToursResponse>() {
            @Override
            public void onResponse(Call<ListToursResponse> call, Response<ListToursResponse> response) {
                if(response.isSuccessful())
                {
                    ArrayList<Tour> list= (ArrayList<Tour>) response.body().getTours();
                    listView=(RecyclerView) findViewById(R.id.rvTours);
                    adapter=new MyAdapter(list);
                    listView.setAdapter(adapter);
                    listView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    countTour=(TextView)findViewById(R.id.countTour);
                    countTour.setText(response.body().getTotal().toString()+ " trips");
                }
            }
            @Override
            public void onFailure(Call<ListToursResponse> call, Throwable t) {
            }

        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateTourActivity.class);
                intent.putExtra("token",Token);
                startActivity(intent);

            }
        });
        editsearch = (SearchView) findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.optionLogout:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    Dialog dialog;
    Button btnYes, btnNo;
    ImageButton imgBtnClose;

    private void showDialog() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Do you want to sign out?");
        btnYes = (Button) dialog.findViewById(R.id.btnYes);
        btnNo = (Button) dialog.findViewById(R.id.btnNo);

        dialog.show();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("isLogined","no");
                editor.commit();

                finish();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }
}
