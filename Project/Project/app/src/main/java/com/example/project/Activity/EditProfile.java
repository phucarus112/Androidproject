package com.example.project.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.ResponseBody;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.UserInfoResponse;
import com.example.project.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfile extends AppCompatActivity {
    private static final String TAG="EditProfile";
    private DatePickerDialog.OnDateSetListener mDateSetListenr;
    private ImageView mDisplayDate;
    private TextView SetDate;
    private EditText name,phone;
    private RadioButton boy,girl;
    String Token;
    Button btnUpdate;
    String dateUpdate="";
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getCompenent();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditProfile.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenr = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);
                String date =dayOfMonth + "/" + month + "/" + year;
                SetDate.setText(date);
                dateUpdate="";
                dateUpdate += year;
                if(month<10) dateUpdate+="-0"+month+"-";
                else dateUpdate+="-"+month+"-";
                if(dayOfMonth<10) dateUpdate+="0"+dayOfMonth;
                else dateUpdate+=dayOfMonth;
                dateUpdate+="T00:00:00.000Z";

            }
        };
        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getUsetInfo(Token)
                .enqueue(new Callback<UserInfoResponse>() {
                    @Override
                    public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                        if(response.isSuccessful())
                        {
                            name.setText(response.body().getFullName());

                            if(response.body().getGender()==1)
                                girl.setChecked(true);
                            else
                            if(response.body().getGender()==0)
                                boy.setChecked(true);
                            String resultDay = response.body().getDob();

                            dateUpdate = response.body().getDob();
                            if(resultDay!=null) {
                                SetDate.setText(String.valueOf(resultDay.charAt(8)) + String.valueOf(resultDay.charAt(9)) + "/" +
                                        String.valueOf(resultDay.charAt(5)) + String.valueOf(resultDay.charAt(6)) + "/" +
                                        String.valueOf(resultDay.charAt(0)) + String.valueOf(resultDay.charAt(1)) +
                                        String.valueOf(resultDay.charAt(2)) + String.valueOf(resultDay.charAt(3)));
                            }
                        }
                        else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(EditProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfoResponse> call, Throwable throwable) {
                        Toast.makeText(EditProfile.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("up",dateUpdate);

              int gender=0;
                    if(boy.isChecked()==true)gender  = 0;
                    else if(girl.isChecked()== true )gender  = 1;


                Retrofit retrofit1 = RetrofitClient.getClient();
                APIService apiService1 = retrofit1.create(APIService.class);
                apiService1.updateUserInfo(Token,name.getText().toString().trim(),gender,dateUpdate)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful())
                                {
                                    Toast.makeText(EditProfile.this, "Update successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                        Toast.makeText(EditProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                Toast.makeText(EditProfile.this, "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }
    private void getCompenent() {
        name = (EditText) findViewById(R.id.etEditFullName);
        btnUpdate = (Button) findViewById(R.id.btnUpdateUserInfo);
        boy = (RadioButton) findViewById(R.id.boy);
        girl = (RadioButton) findViewById(R.id.girl);
        mDisplayDate = (ImageView) findViewById(R.id.buttonCaledar);
        SetDate = (TextView) findViewById(R.id.tvDob);
        Token = getIntent().getStringExtra("token");
        radioGroup=(RadioGroup)findViewById(R.id.radioGender);
    }
}

