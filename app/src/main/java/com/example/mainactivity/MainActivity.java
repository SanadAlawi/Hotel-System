package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mainActivity_btn_login;
    private Button mainActivity_btn_sign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity_btn_login = findViewById(R.id.mainActivity_btn_login);
        mainActivity_btn_sign = findViewById(R.id.mainActivity_btn_signup);
    }

    public void Login(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    public void SignUp(View view) {
        Intent loginIntent = new Intent(this, SignupCustomerActivity.class);
        startActivity(loginIntent);
    }
}