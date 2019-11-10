package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    TextView tvForgetPass, tvSignUp;
    EditText etEmailPhone, etPassWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getComponent();

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Activity_Forget.class);
                startActivity(intent);
            }
        });
    }

    private void getComponent() {
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvForgetPass = (TextView) findViewById(R.id.tvForgetPass);
        etEmailPhone = (EditText) findViewById(R.id.etEmailPhone);
        etPassWord = (EditText) findViewById(R.id.etPassWord);
    }


}
