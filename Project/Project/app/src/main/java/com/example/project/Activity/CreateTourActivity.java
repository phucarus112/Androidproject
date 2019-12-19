package com.example.project.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.CreateTourRequest;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.R;

import java.io.File;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateTourActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private ImageView mDisplayDate1, mDisplayDate2,GgMapSrc,GgMapDes, imgChooseImg;
    private DatePickerDialog.OnDateSetListener mDateSetListenr,mDateSetListenr2;
    private EditText  Name,Adult,Children,MinCost,MaxCost;
    private TextView SetDate1,SetDate2;
    private long milisecondStart, milisecondEnd;
    private Button btnCreate;
    private RadioButton isPrivate;
    public static final int PICK_IMAGE = 1;
    private TextView numsrclat, numsrclong, numdeslat, numdeslong,Source,Des;
    SharedPreferences sharedPreferences,tourSharePreferences;
    SharedPreferences.Editor editor,tourEditor;
    public static  final String SRC= "SRC";

    private int num;
    private  String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);

        token= getIntent().getStringExtra("token");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Tour");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getComponent();
        sharedPreferences= getSharedPreferences("data",MODE_PRIVATE);
        editor=  sharedPreferences.edit();
        GetSharePre();
        GetDateMap();
        tourSharePreferences = getSharedPreferences("tourSP",MODE_PRIVATE);
        tourEditor = sharedPreferences.edit();
        tourEditor.putString("isCompleted","no");
        tourEditor.commit();

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

                Intent intent= new Intent(CreateTourActivity.this, StopPointActivity.class);
                num=1;
                intent.putExtra(SRC,num);
                intent.putExtra("token",token);
                PushIntoSharePre();
                startActivity(intent);
                SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP",MODE_PRIVATE);
                if(sharedPreferences4.getString("hoan thanh","").equals("ok"))
                {
                    SharedPreferences.Editor editor4 =sharedPreferences4.edit();
                    editor4.putString("hoan thanh","not ok");
                    editor4.commit();
                    finish();
                }

            }
        });
        GgMapDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateTourActivity.this, StopPointActivity.class);
                num = 2;
                intent.putExtra(SRC, num);
                intent.putExtra("token", token);
                PushIntoSharePre();
                startActivity(intent);
                SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP",MODE_PRIVATE);
                if(sharedPreferences4.getString("hoan thanh","").equals("ok"))
                {
                    SharedPreferences.Editor editor4 =sharedPreferences4.edit();
                    editor4.putString("hoan thanh","not ok");
                    editor4.commit();
                    finish();
                }
            }
        });

        imgChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String param = (isPrivate.isChecked()) ? "true" : "false";
                Retrofit retrofit = RetrofitClient.getClient();
                APIService apiService = retrofit.create(APIService.class);
                if (Name.getText().toString().trim().isEmpty() == false && SetDate1.getText().toString().trim().isEmpty() == false &&
                        SetDate2.getText().toString().trim().isEmpty() == false && Source.getText().toString().trim().isEmpty() == false &&
                        Des.getText().toString().trim().isEmpty() == false && Adult.getText().toString().trim().isEmpty() == false &&
                        Children.getText().toString().trim().isEmpty() == false && MinCost.getText().toString().trim().isEmpty() == false &&
                        MaxCost.getText().toString().trim().isEmpty() == false ) {
                    apiService.createTour(token, Name.getText().toString().trim()
                            , milisecondStart, milisecondEnd
                            , Float.parseFloat(sharedPreferences.getString("numsrclat", "")),
                            Float.parseFloat(sharedPreferences.getString("numsrclong", "")),
                            Float.parseFloat(sharedPreferences.getString("numdeslat", "")),
                            Float.parseFloat(sharedPreferences.getString("numdeslong", "")),
                            Boolean.parseBoolean(param),
                            Integer.parseInt(Adult.getText().toString()), Integer.parseInt(Children.getText().toString()),
                            Integer.parseInt(MinCost.getText().toString()), Integer.parseInt(MaxCost.getText().toString()),null)
                            .enqueue(new Callback<CreateTourRequest>() {
                                @Override

                                public void onResponse(Call<CreateTourRequest> call, Response<CreateTourRequest> response) {
                                    if (response.isSuccessful()) {

                                        Log.e("xem thu",sharedPreferences.getString("numsrclat", "")
                                               +"///"+ sharedPreferences.getString("numsrclong", "")
                                                + "///"+sharedPreferences.getString("numdeslat", "")
                                            +"///"+ sharedPreferences.getString("numdeslong", ""));
                                        Log.e("idtour", String.valueOf(response.body().getId()));

                                        tourSharePreferences = getSharedPreferences("tourSP",MODE_PRIVATE);
                                        tourEditor = tourSharePreferences.edit();
                                        tourEditor.putString("isCompleted","yes");
                                        tourEditor.commit();

                                        SharedPreferences preferences = getSharedPreferences("saveSP",MODE_PRIVATE);
                                        SharedPreferences.Editor editor1 = preferences.edit();
                                        editor1.putString("tim sau khi tao","yes");
                                        editor1.commit();

                                        Toast.makeText(CreateTourActivity.this, "Succesffully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CreateTourActivity.this, StopPointActivity.class);
                                        intent.putExtra("token",token);
                                        intent.putExtra("tourId",String.valueOf(response.body().getId()));
                                        num = 3;
                                        intent.putExtra(SRC, num);
                                        startActivity(intent);
                                        finish();
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
                else  Toast.makeText(CreateTourActivity.this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP",MODE_PRIVATE);
        if(sharedPreferences4.getString("hoan thanh","").equals("ok"))
        {
            SharedPreferences.Editor editor4 =sharedPreferences4.edit();
            editor4.putString("hoan thanh","not ok");
            editor4.commit();
            finish();
        }
    }

    private void PushIntoSharePre(){
        editor.putString("nametour",Name.getText().toString().trim());
        editor.putLong("milisecondStart",milisecondStart);
        editor.putString("startday",SetDate1.getText().toString());
        editor.putLong("milisecondEnd",milisecondEnd);
        editor.putString("endday",SetDate2.getText().toString());
        editor.putString("adult",Adult.getText().toString());
        editor.putString("children",Children.getText().toString());
        editor.putString("mincost", MinCost.getText().toString());
        editor.putString("maxcost", MaxCost.getText().toString());
        editor.commit();
    }
    private void GetDateMap(){
        if(getIntent().getIntExtra(StopPointActivity.TEMP,0)==5) {
            if (getIntent().getIntExtra(StopPointActivity.SRC, 0) == 1) {

                Source.setText(getIntent().getStringExtra(StopPointActivity.ADDRESS));
                editor.putString("source", getIntent().getStringExtra(StopPointActivity.ADDRESS));
                editor.putString("numsrclat", getIntent().getStringExtra(StopPointActivity.SRCLAT));
                editor.putString("numsrclong", getIntent().getStringExtra(StopPointActivity.SRCLONG));
                editor.commit();

            }
            if (getIntent().getIntExtra(StopPointActivity.SRC, 0) == 2) {

                Des.setText(getIntent().getStringExtra(StopPointActivity.ADDRESS));
                editor.putString("des", getIntent().getStringExtra(StopPointActivity.ADDRESS));
                editor.putString("numdeslat", getIntent().getStringExtra(StopPointActivity.SRCLAT));
                editor.putString("numdeslong", getIntent().getStringExtra(StopPointActivity.SRCLONG));
                editor.commit();
            }
        }else if(getIntent().getIntExtra(StopPointActivity.TEMP,0)==5) {
            Source.setText(sharedPreferences.getString("source",""));
            Des.setText(sharedPreferences.getString("des",""));
        }
    }
    private  void GetSharePre(){
        Name.setText(sharedPreferences.getString("nametour",""));
        SetDate1.setText(sharedPreferences.getString("startday",""));
        SetDate2.setText(sharedPreferences.getString("endday",""));
        Source.setText(sharedPreferences.getString("source",""));
        Des.setText(sharedPreferences.getString("des",""));
        Adult.setText(sharedPreferences.getString("adult",""));
        Children.setText(sharedPreferences.getString("children",""));
        MinCost.setText(sharedPreferences.getString("mincost",""));
        MaxCost.setText(sharedPreferences.getString("maxcost",""));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data == null) return;
            Uri selectedImage = data.getData();
            File myFile = new File(selectedImage.toString());
            String path = myFile.getName();
            Log.e("name",path);
        }
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
        SetDate1= (TextView) findViewById(R.id.etStartDay);
        mDisplayDate2 = (ImageView) findViewById(R.id.IMcalendar2);
        SetDate2= (TextView) findViewById(R.id.etEndDay);
        Name =(EditText) findViewById(R.id.etFullName);
        Adult= (EditText) findViewById(R.id.etAdult);
        Children= (EditText) findViewById(R.id.etChildren);
        MinCost = (EditText) findViewById(R.id.etMinCost);
        MaxCost= (EditText) findViewById(R.id.etMaxCost);
        btnCreate=(Button)findViewById(R.id.btnCreateTour);
        isPrivate= (RadioButton) findViewById(R.id.BtnIsPrivate);
        GgMapSrc=(ImageView) findViewById(R.id.ImGgMapSrc);
        GgMapDes=(ImageView) findViewById(R.id.ImGgMapDes);
        imgChooseImg = (ImageView) findViewById(R.id.imgChooseImg);
        Source=(TextView)findViewById(R.id.tvSrc);
        Des=(TextView)findViewById(R.id.tvDes);

    }
}