package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.UserInfoResponse;
import com.example.project.Data.ServiceType;
import com.example.project.R;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivitySetting extends AppCompatActivity {
    private Spinner spinner;
    private TextView edit,editPass;
    TextView nameUser;
    String Token;
    Button logout;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        spinner=(Spinner) findViewById(R.id.spinnerlanguage);
        edit=(TextView)findViewById(R.id.EditProfile);
        editPass = (TextView) findViewById(R.id.EditProfilePass);
        nameUser = (TextView) findViewById(R.id.nameUser);
        Token = getIntent().getStringExtra("token");
       logout= (Button) findViewById(R.id.btnLogout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_setting);
        bottomNavigationView.setSelectedItemId(R.id.navigation_Explore);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_ListTour:
                        Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent = new Intent(ActivitySetting.this, MainActivity.class);
                        intent.putExtra("token", Token);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_History:
                        Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent2 = new Intent(ActivitySetting.this, HistoryActivity.class);
                        intent2.putExtra("token", Token);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_Explore:
                        Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent3 = new Intent(ActivitySetting.this, ExploreActivity.class);
                        intent3.putExtra("token", Token);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_Notifications:
                        //Toast.makeText(com.example.project.Activity.MainActivity.this, "notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_Setting:
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });

        final ArrayList<String> arrayLanguage = getLanguage();
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayLanguage);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySetting.this, EditProfile.class);
                intent.putExtra("token", Token);
                startActivity(intent);
            }
        });

        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getUsetInfo(Token)
                .enqueue(new Callback<UserInfoResponse>() {
                    @Override
                    public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                        if(response.isSuccessful())
                        {
                            nameUser.setText(response.body().getFullName());
                            userId = response.body().getId();
                        }
                        else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ActivitySetting.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfoResponse> call, Throwable throwable) {
                        Toast.makeText(ActivitySetting.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });

        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ActivitySetting.this, UpdatePassword.class);
                intent.putExtra("token", Token);
                intent.putExtra("userId",String.valueOf(userId));
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetting.this);
                builder.setMessage("Do you want to exit app?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("isLogined","no");
                                if(sharedPreferences.getString("isLoginedFB","").equals("yes"))
                                {
                                    LoginManager.getInstance().logOut();
                                    editor.putString("isLoginedFB","no");
                                }
                                editor.commit();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getUsetInfo(Token)
                .enqueue(new Callback<UserInfoResponse>() {
                    @Override
                    public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                        if(response.isSuccessful())
                        {
                            nameUser.setText(response.body().getFullName());
                        }
                        else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ActivitySetting.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfoResponse> call, Throwable throwable) {
                        Toast.makeText(ActivitySetting.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });

        SharedPreferences sharedPreferences = getSharedPreferences("out", MODE_PRIVATE);
        if(sharedPreferences.getString("dadoimatkhau","").equals("yes"))
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("dadoimatkhau","no");
            editor.commit();
            finish();
        }
    }

    private ArrayList<String> getLanguage() {
        ArrayList<String> temp  =new ArrayList<>();

        ArrayList<ServiceType> LanguageArrayList = new ArrayList<>();
        LanguageArrayList.add(new ServiceType(1,"English"));
        LanguageArrayList.add(new ServiceType(2,"Vietnamese"));

        for(int i=0;i< LanguageArrayList.size();i++)
        {
            temp.add( LanguageArrayList.get(i).getService_name());
        }

        return temp;
    }

}
