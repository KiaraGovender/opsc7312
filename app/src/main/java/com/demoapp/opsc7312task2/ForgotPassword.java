package com.demoapp.opsc7312task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity
{
    Button resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetPassword = findViewById(R.id.btn_resetPassword);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ForgotPassword.this, "Functionality still in development. Please contact the administrator!",
                        Toast.LENGTH_LONG).show();

                Intent openNewActivity = new Intent(ForgotPassword.this, MainActivity.class);
                startActivity(openNewActivity);
            }
        });
    }
}