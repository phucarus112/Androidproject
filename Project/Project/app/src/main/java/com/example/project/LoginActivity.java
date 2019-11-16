package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.LoginRequest;
import com.example.project.APIConnect.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    TextView tvForgetPass, tvSignUp;
    EditText etEmailPhone, etPassWord;
    Button btnSignIn;
    CheckBox cbRemember;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getComponent();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = RetrofitClient.getClient();
                APIService apiService = retrofit.create(APIService.class);

                apiService.login(etEmailPhone.getText().toString().trim(), etPassWord.getText().toString().trim())
                        .enqueue(new Callback<LoginRequest>() {
                            @Override
                            public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
                                if (response.isSuccessful()) {
                                    //luu vao shared prefrence neu checkboc duoc tick
                                    String token=response.body().getToken();

                                    if (cbRemember.isChecked()) {
                                        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                        if (sharedPreferences.contains("login") == false) {
                                            //neu khong chua thi ghi vo
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            //ghi nd cai moi
                                            editor.putString("emailPhone", etEmailPhone.getText().toString().trim());
                                            editor.putString("password", etPassWord.getText().toString().trim());
                                            editor.putBoolean("checked", true);
                                            editor.commit();
                                            Log.e("Da luu", "da luu");
                                        }
                                    }
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("token",token);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "Wrong email/phone or password", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<LoginRequest> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Activity_Forget.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        etEmailPhone.setText(sharedPreferences.getString("emailPhone", ""));
        etPassWord.setText(sharedPreferences.getString("password", ""));
        cbRemember.setChecked(sharedPreferences.getBoolean("checked",false));
    }

    private void getComponent() {
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvForgetPass = (TextView) findViewById(R.id.tvForgetPass);
        etEmailPhone = (EditText) findViewById(R.id.etEmailPhone);
        etPassWord = (EditText) findViewById(R.id.etPassWord);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
    }

}
