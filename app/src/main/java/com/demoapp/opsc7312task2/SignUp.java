package com.demoapp.opsc7312task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("logins");
    EditText fullName, email, password, confirmPassword;
    Button signUp, signIn;
    private FirebaseAuth mAuth;
    UserLogin userLogins;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.edtName);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        confirmPassword = findViewById(R.id.edtConfPass);
        signUp = findViewById(R.id.btnSign);
        signIn = findViewById(R.id.btnLogIN);

        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openNewActivity = new Intent(SignUp.this, Login.class);
                startActivity(openNewActivity);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    final String enteredFullName = fullName.getText().toString().trim();
                    String enteredEmail = email.getText().toString().trim();
                    String enteredPassword = password.getText().toString().trim();
                    String enteredPasswordConfirmation = confirmPassword.getText().toString().trim();

                    userLogins = new UserLogin(enteredFullName, enteredEmail);

                    if (enteredPassword.equals(enteredPasswordConfirmation))
                    {
                        mAuth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            myRef.push().setValue(userLogins);
                                            Toast.makeText(SignUp.this, "User "
                                                            + mAuth.getCurrentUser().getEmail() + "successfully registered. Please setup your profile.",
                                                    Toast.LENGTH_SHORT).show();

                                            Intent openNewActivity = new Intent(SignUp.this, Login.class);
                                            openNewActivity.putExtra("FullName", enteredFullName);
                                            startActivity(openNewActivity);
                                        }

                                        else
                                        {
                                            Toast.makeText(SignUp.this, "Error! Not registered",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(SignUp.this, "Password and Password Confirmation entered do not match.", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(SignUp.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}