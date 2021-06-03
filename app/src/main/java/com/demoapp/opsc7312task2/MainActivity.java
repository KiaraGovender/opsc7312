package com.demoapp.opsc7312task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.btnSignUp);

        signup.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
        });

        login.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });

    }
}