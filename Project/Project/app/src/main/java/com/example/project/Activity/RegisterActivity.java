package com.example.project.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.LoginRequest;
import com.example.project.APIConnect.RegisterRequest;
import com.example.project.APIConnect.RegisterResponse;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName,etEmail, etPhone,etPassword,etConfirmPassword;
    Button btnSignUp;
    SharedPreferences sharedPreferences;
    private ImageView mDisplayDate1;
    private DatePickerDialog.OnDateSetListener mDateSetListenr;
    TextView SetDate1;
    EditText etAddress;
    CheckBox nam,nu;
    private long milisecondStart, milisecondEnd;
    String dayPost="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getComponent();

        mDisplayDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                milisecondStart = cal.getTimeInMillis();
                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenr = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                dayPost += year;
                if(month<10)dayPost+="-0"+month+"-";
                else dayPost+="-"+month+"-";
                if(dayOfMonth<10) dayPost+= "0"+dayOfMonth;
                else dayPost+=dayOfMonth;
                dayPost+="T00:00:00.000Z";
                Log.e("daypost",dayPost);
                SetDate1.setText(date);
            }
        };

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etFullName.getText().toString().trim().equals("") || etAddress.getText().toString().trim().equals("")
                || etEmail.getText().toString().trim().equals("") || etPhone.getText().toString().trim().equals("")
                || etPassword.getText().toString().trim().equals("") || etConfirmPassword.getText().toString().trim().equals("")
                || SetDate1.getText().toString().trim().equals("") || (!nam.isChecked() && !nu.isChecked())) {
                    Toast.makeText(RegisterActivity.this, "Please fill fully data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(nam.isChecked() && nu.isChecked())
                    {
                        Toast.makeText(RegisterActivity.this, "Cannot choose 2 gender", Toast.LENGTH_SHORT).show();
                    }
                    else if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))
                    {
                        Toast.makeText(RegisterActivity.this, "Password is different from Confirm Password", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        final Retrofit retrofit = RetrofitClient.getClient();
                        final APIService apiService = retrofit.create(APIService.class);

                        int gioitinh; //1: nam, 0:nu
                        if(nam.isChecked()) gioitinh =1;
                        else gioitinh = 0;

                        apiService.signup(etFullName.getText().toString().trim(),etEmail.getText().toString().trim(),
                            etPhone.getText().toString().trim(),etPassword.getText().toString().trim(),
                                etAddress.getText().toString().trim(),gioitinh,dayPost)
                            .enqueue(new Callback<RegisterRequest>() {
                                @Override
                                public void onResponse(Call<RegisterRequest> call, Response<RegisterRequest> response) {
                                    if(response.isSuccessful())
                                    {
                                        APIService apiService1 = retrofit.create(APIService.class);
                                        final String temp = response.body().getEmail();
                                        apiService1.login(response.body().getEmail(),etPassword.getText().toString().trim())
                                                .enqueue(new Callback<LoginRequest>() {
                                                    @Override
                                                    public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
                                                        if(response.isSuccessful())
                                                        {
                                                            sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("isLogined","yes");
                                                            editor.putString("emailPhone",temp);
                                                            editor.putString("password",etPassword.getText().toString().trim());
                                                            editor.commit();
                                                            String token=response.body().getToken();
                                                            SharedPreferences sharedPreferences2 = getSharedPreferences("tokenUser",MODE_PRIVATE);
                                                            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                                            editor2.putString("token",response.body().getToken());
                                                            editor2.commit();
                                                            Log.e("token s khi dki",response.body().getToken());
                                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                            intent.putExtra("token",token);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<LoginRequest> call, Throwable t) {
                                                        Toast.makeText(RegisterActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        if(response.code() == 400)
                                        {
                                            try {
                                                String alert = response.errorBody().string();
                                                String[] token  = alert.split(":");
                                                if(token[4].charAt(1)== 'p')
                                                Toast.makeText(RegisterActivity.this, "Phone already registered", Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(RegisterActivity.this,"Email already registered", Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else if(response.code() == 503)
                                        {
                                            Toast.makeText(RegisterActivity.this, "Server error on creating user", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<RegisterRequest> call, Throwable t) {
                                    Toast.makeText(RegisterActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                }
            }
        });
    }

    private void getComponent() {
        etFullName = (EditText)findViewById(R.id.etFullName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btnSignUp = (Button) findViewById(R.id.btnRegister);
        mDisplayDate1 = (ImageView) findViewById(R.id.imgDobDki);
        SetDate1= (TextView) findViewById(R.id.tvDobDki);
        etAddress = (EditText) findViewById(R.id.etAddressDki);
        nam = (CheckBox) findViewById(R.id.cbMaleDki);
        nu  =(CheckBox) findViewById(R.id.cbFemaleDki);
    }
}
