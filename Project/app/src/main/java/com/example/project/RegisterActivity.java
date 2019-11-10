package com.example.project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG="LoginActivity";
    private TextView tvbirthday;
    private Button btnBirthDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
    }

//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Calendar c= Calendar.getInstance();
//        c.set(Calendar.YEAR,year);
//        c.set(Calendar.MONTH,month);
//        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
//        tvbirthday.setText(currentDateString);
//    }
}

