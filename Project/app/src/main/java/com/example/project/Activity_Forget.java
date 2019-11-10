package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity_Forget extends AppCompatActivity {
    Button mess,gmail,submit;
    EditText e;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__forget);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mess=(Button)findViewById(R.id.mess);
        gmail=(Button)findViewById(R.id.Gmail);
        submit=(Button)findViewById(R.id.submit);
        e=(EditText)findViewById(R.id.edtext);
        i=0;//mess
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                gmail.setBackgroundResource(R.drawable.et_box);
                mess.setBackgroundResource(R.drawable.buttonan);
                e.setHint("Enter your gmail");
            }
        });
        mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=0;
                gmail.setBackgroundResource(R.drawable.buttonan);
                mess.setBackgroundResource(R.drawable.et_box);
                e.setHint("Enter your phone number");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
