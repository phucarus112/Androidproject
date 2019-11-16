package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.RegisterRequest;
import com.example.project.APIConnect.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName,etEmail, etPhone,etPassword,etConfirmPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getComponent();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = RetrofitClient.getClient();
                APIService apiService = retrofit.create(APIService.class);

                apiService.signup(etFullName.getText().toString().trim(),etEmail.getText().toString().trim(),
                        etPhone.getText().toString().trim(),etPassword.getText().toString().trim())
                        .enqueue(new Callback<RegisterRequest>() {
                            @Override
                            public void onResponse(Call<RegisterRequest> call, Response<RegisterRequest> response) {
                                if(response.isSuccessful())
                                {
                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RegisterRequest> call, Throwable t) {
                                Toast.makeText(RegisterActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
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
    }
}
//        tvbirthday= (TextView) findViewById(R.id.tvBirthday);
//        btnBirthDay= (Button) findViewById(R.id.GotoBirthday);
//
//        btnBirthDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment datePicker= new DatePickerFragment();
//                datePicker.show(getSupportFragmentManager(),"datePicker");
//            }
//        });

//        Intent incomingIntent = getIntent();
//        String date= incomingIntent.getStringExtra("date");
//        tvbirthday.setText(date);
//
//        btnBirthDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(intent);
//            }
//        });
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Calendar c= Calendar.getInstance();
//        c.set(Calendar.YEAR,year);
//        c.set(Calendar.MONTH,month);
//        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
//        tvbirthday.setText(currentDateString);
//    }


