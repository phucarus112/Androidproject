package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.CreateTourRequest;
import com.example.project.APIConnect.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateTourActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private ImageView mDisplayDate1, mDisplayDate2,GgMapSrc,GgMapDes;
    private DatePickerDialog.OnDateSetListener mDateSetListenr,mDateSetListenr2;
    private EditText SetDate1,SetDate2, Name,Adult,Children,MinCost,MaxCost;
    private long milisecondStart, milisecondEnd;
    private Button btnCreate;
    private RadioButton isPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Tour");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        CreateTourActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                milisecondEnd = cal.getTimeInMillis();
                DatePickerDialog dialog = new DatePickerDialog(
                        CreateTourActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr2, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListenr = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                SetDate1.setText(date);
            }
        };
        mDateSetListenr2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                SetDate2.setText(date);
            }
        };
        GgMapSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CreateTourActivity.this,StopPointActivity.class);
                startActivity(intent);
            }
        });
        GgMapDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CreateTourActivity.this,StopPointActivity.class);
                startActivity(intent);
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String param = (isPrivate.isChecked()) ? "true" : "false";

                Retrofit retrofit = RetrofitClient.getClient();
                APIService apiService = retrofit.create(APIService.class);

                apiService.createTour(getIntent().getStringExtra("token"), Name.getText().toString().trim()
                        , milisecondStart, milisecondEnd
                        , 0F,0F,0F,0F, Boolean.parseBoolean(param),
                        Integer.parseInt(Adult.getText().toString()),Integer.parseInt(Children.getText().toString()),
                        Integer.parseInt(MinCost.getText().toString()),Integer.parseInt(MaxCost.getText().toString()))
                        .enqueue(new Callback<CreateTourRequest>() {
                            @Override
                            public void onResponse(Call<CreateTourRequest> call, Response<CreateTourRequest> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(CreateTourActivity.this, "succesffuflgfnd", Toast.LENGTH_SHORT).show();
                                    //Intent intent = new Intent(CreateTourActivity.this, StopPointActivity.class);
                                    //startActivity(intent);
                                } else {
                                    Toast.makeText(CreateTourActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<CreateTourRequest> call, Throwable t) {
                                Toast.makeText(CreateTourActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getComponent()
    {
        mDisplayDate1 = (ImageView) findViewById(R.id.IMcalendar);
        SetDate1= (EditText) findViewById(R.id.etStartDay);
        mDisplayDate2 = (ImageView) findViewById(R.id.IMcalendar2);
        SetDate2= (EditText) findViewById(R.id.etEndDay);
        Name =(EditText) findViewById(R.id.etFullName);
        Adult= (EditText) findViewById(R.id.etAdult);
        Children= (EditText) findViewById(R.id.etChildren);
        MinCost = (EditText) findViewById(R.id.etMinCost);
        MaxCost= (EditText) findViewById(R.id.etMaxCost);
        btnCreate=(Button)findViewById(R.id.btnCreateTour);
        isPrivate= (RadioButton) findViewById(R.id.BtnIsPrivate);
        GgMapSrc=(ImageView) findViewById(R.id.ImGgMapSrc);
        GgMapDes=(ImageView) findViewById(R.id.ImGgMapDes);
    }
}
