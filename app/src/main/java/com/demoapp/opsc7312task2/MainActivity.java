package com.demoapp.opsc7312task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.location.DefaultLocationProvider;

public class MainActivity extends AppCompatActivity
{
    Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        MapboxSearchSdk.initialize(this.getApplication(), getString(R.string.access_token),
                new DefaultLocationProvider(this.getApplication()));

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