package com.example.project.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.OTPResponse;
import com.example.project.APIConnect.ResponseBody;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.R;
import com.facebook.login.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Activity_Forget extends AppCompatActivity {
    Button submit;
    EditText editText,etOtp,etNewPass;
    ImageView send;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__forget);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        editText=(EditText)findViewById(R.id.edtext);
        submit = (Button) findViewById(R.id.submit);
        etOtp = (EditText)findViewById(R.id.etOtpFP);
        etNewPass = (EditText) findViewById(R.id.etnewPassFP);
        send = (ImageView) findViewById(R.id.imgSendFP);

        etOtp.setActivated(false);
        etNewPass.setActivated(false);
        submit.setActivated(false);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().equals(""))
                {
                    Toast.makeText(Activity_Forget.this, "Please input your email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Retrofit retrofit = RetrofitClient.getClient();
                    APIService apiService = retrofit.create(APIService.class);
                    apiService.requestOTP("email",editText.getText().toString().trim())
                            .enqueue(new Callback<OTPResponse>() {
                                @Override
                                public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                                    if(response.isSuccessful())
                                    {
                                        userId = response.body().getUserId();
                                        Toast.makeText(Activity_Forget.this, "OTP code has sent to your email. Please check it", Toast.LENGTH_SHORT).show();
                                        etOtp.setActivated(true);
                                        etNewPass.setActivated(true);
                                        submit.setActivated(true);
                                    }
                                    else
                                    {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            Toast.makeText(Activity_Forget.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<OTPResponse> call, Throwable t) {
                                    Toast.makeText(Activity_Forget.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etOtp.getText().toString().trim().equals("") || etNewPass.getText().toString().trim().equals(""))
                {
                    Toast.makeText(Activity_Forget.this, "Please fill full data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Retrofit retrofit = RetrofitClient.getClient();
                    APIService apiService = retrofit.create(APIService.class);
                    apiService.verifyOTP(userId,etNewPass.getText().toString().trim(),etOtp.getText().toString().trim())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful())
                                    {
                                        Toast.makeText(Activity_Forget.this,"Your password has been changed", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Activity_Forget.this, LoginActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            Toast.makeText(Activity_Forget.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(Activity_Forget.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
