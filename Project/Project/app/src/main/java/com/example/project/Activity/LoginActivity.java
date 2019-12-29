package com.example.project.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.LoginRequest;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    CallbackManager callbackManager;
    LoginButton fbLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getFacebookComponent();
        getComponent();

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                Retrofit retrofit = RetrofitClient.getClient();
                APIService apiService = retrofit.create(APIService.class);
                Log.e("token",loginResult.getAccessToken().getToken());

                apiService.loginFacebook(loginResult.getAccessToken().getToken())
                        .enqueue(new Callback<LoginRequest>() {
                            @Override
                            public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response){
                                if(response.isSuccessful())
                                {
                                    String token=response.body().getToken();
                                    //Toast.makeText(LoginActivity.this, "Login Facebook successfully", Toast.LENGTH_SHORT).show();
                                    sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("isLogined","yes");
                                    editor.putString("isLoginedFB","yes");
                                    editor.putString("tokenFB",token);
                                    editor.commit();
                                    SharedPreferences sharedPreferences2 = getSharedPreferences("tokenUser",MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                    editor2.putString("userId", String.valueOf(response.body().getUserId()));
                                    editor2.putString("token",response.body().getToken());
                                    editor2.commit();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    intent.putExtra("token",token);
                                    startActivity(intent);
                                }
                                else
                                {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginRequest> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login Facebook cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Login Facebook error.", Toast.LENGTH_SHORT).show();
            }
        });

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
                                            editor.putString("isLogined","yes");
                                            editor.commit();
                                        }
                                    }
                                    else
                                    {
                                        SharedPreferences.Editor myEditor = sharedPreferences.edit();
                                       myEditor.putString("emailPhone", etEmailPhone.getText().toString().trim());
                                        myEditor.putString("password", etPassWord.getText().toString().trim());
                                       myEditor.putString("isLogined","yes");
                                        myEditor.putBoolean("checked",false);
                                        myEditor.commit();
                                    }

                                    SharedPreferences sharedPreferences2 = getSharedPreferences("tokenUser",MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                    editor2.putString("userId", String.valueOf(response.body().getUserId()));
                                    editor2.commit();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("token",token);
                                    startActivity(intent);
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                        Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
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

    private void getFacebookComponent() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        fbLoginButton = (LoginButton) findViewById(R.id.btnFacebook);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        etEmailPhone.setText(sharedPreferences.getString("emailPhone", ""));
        etPassWord.setText(sharedPreferences.getString("password", ""));
        cbRemember.setChecked(sharedPreferences.getBoolean("checked",false));

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
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