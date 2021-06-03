package com.demoapp.opsc7312task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity
{
    EditText email, password;
    Button login, register, forgotPassword;
    CheckBox rememberMe;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.edtPass);
        login = findViewById(R.id.btnLogIn);
        register = findViewById(R.id.btnSignUP);
        forgotPassword = findViewById(R.id.btnForgotPass);
        rememberMe = findViewById(R.id.cbRem);

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openNewActivity = new Intent(Login.this, SignUp.class);
                startActivity(openNewActivity);
            }
        });

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String enteredEmail = email.getText().toString().trim();
                String enteredPassword = password.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(enteredEmail, enteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Login.this, "Logged in "
                                            + mAuth.getCurrentUser().getEmail() + " successfully",
                                    Toast.LENGTH_SHORT).show();

                            Intent openNewActivity = new Intent(Login.this, Mapmain.class);
                            startActivity(openNewActivity);

                            //Clear entered credentials
                            email.setText("");
                            password.setText("");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openNewActivity = new Intent(Login.this, ForgotPassword.class);
                startActivity(openNewActivity);
            }
        });
    }
}