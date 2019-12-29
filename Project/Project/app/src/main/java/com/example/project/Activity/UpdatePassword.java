package com.example.project.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.ResponseBody;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdatePassword extends AppCompatActivity {

    EditText cur, newPass;
    EditText confirm;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        cur = (EditText) findViewById(R.id.curpass);
        newPass = (EditText) findViewById(R.id.newPass);
        confirm = (EditText) findViewById(R.id.confirmnewpass);
        btn = (Button) findViewById(R.id.updatePass);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirm.getText().toString().trim().equals(newPass.getText().toString().trim()))
                {
                    Retrofit retrofit = RetrofitClient.getClient();
                    APIService apiService = retrofit.create(APIService.class);
                    Log.e("a",getIntent().getStringExtra("userId"));
                    apiService.updatePassword(getIntent().getStringExtra("token"), Integer.parseInt(getIntent().getStringExtra("userId")),
                            cur.getText().toString().trim(), newPass.getText().toString().trim())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(UpdatePassword.this, "Update password successfully", Toast.LENGTH_SHORT).show();
                                        SharedPreferences sharedPreferences = getSharedPreferences("out", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("dadoimatkhau","yes");
                                        editor.commit();

                                        finish();
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            Toast.makeText(UpdatePassword.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                    Toast.makeText(UpdatePassword.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else   Toast.makeText(UpdatePassword.this, "New password is different", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
