package com.demoapp.opsc7312task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    String unitsMeasured, speedCameras, speedLimits, traffic, roadConstruction;
    //String speedCameras = "true";
    //String speedLimits = "true";
    //String traffic = "true";
    //String roadConstruction = "true";
    RadioGroup unitOfMeasurement;
    RadioButton metric, imperial, selectedUnit;
    SwitchMaterial showCameras, showLimits, showTraffic, showConstruction;
    Button save;
    UserSettings userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());

        unitOfMeasurement = findViewById(R.id.radioGroupUnitsOfMeasurement);
        metric = findViewById(R.id.radioMetric);
        imperial = findViewById(R.id.radioImperial);
        showCameras = findViewById(R.id.svSpeedCameras);
        showLimits = findViewById(R.id.svSpeedLimits);
        showTraffic = findViewById(R.id.svTraffic);
        showConstruction = findViewById(R.id.svRoadConstruction);
        save = findViewById(R.id.btnSaveSettings);

        showCameras.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    // The toggle is enabled
                    speedCameras = "true";
                    //Toast.makeText(Settings.this, "SpeedCameras ON", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    speedCameras = "false";
                    //Toast.makeText(Settings.this, "SpeedCameras OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showLimits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    // The toggle is enabled
                    speedLimits = "true";
                    //Toast.makeText(Settings.this, "Show SpeedLimits ON", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    speedLimits = "false";
                    //Toast.makeText(Settings.this, "Show SpeedLimits OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showTraffic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    // The toggle is enabled
                    traffic = "true";
                    //Toast.makeText(Settings.this, "Show Traffic ON", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    traffic = "false";
                    //Toast.makeText(Settings.this, "Show Traffic OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showConstruction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    // The toggle is enabled
                    roadConstruction = "true";
                    //Toast.makeText(Settings.this, "Show Road Construction ON", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    roadConstruction = "false";
                    //Toast.makeText(Settings.this, "Show Road Construction OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myRef.child("Settings").addValueEventListener(new ValueEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userSettings = snapshot.getValue(UserSettings.class);

                if (userSettings != null)
                {
                    try
                    {
                        if (userSettings.getUnitSetting().equals("Metric"))
                        {
                            metric.setChecked(true);
                        }
                        else if (userSettings.getUnitSetting().equals("Imperial"))
                        {
                            imperial.setChecked(true);
                        }

                        if(userSettings.getSpeedCameras().equals("true")) {
                            showCameras.setChecked(true);
                        }
                        else{
                            showCameras.setChecked(false);
                        }

                        if(userSettings.getSpeedLimits().equals("true")) {
                            showLimits.setChecked(true);
                        }
                        else{
                            showLimits.setChecked(false);
                        }

                        if(userSettings.getTraffic().equals("true")) {
                            showTraffic.setChecked(true);
                        }
                        else{
                            showTraffic.setChecked(false);
                        }

                        if(userSettings.getRoadConstruction().equals("true")) {
                            showConstruction.setChecked(true);
                        }
                        else{
                            showConstruction.setChecked(false);
                        }

                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(Settings.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Settings.this, "Please save your selection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Settings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int selectedId = unitOfMeasurement.getCheckedRadioButtonId();
                selectedUnit = (RadioButton) findViewById(selectedId);
                if(selectedId==-1){
                    Toast.makeText(Settings.this,"Unit of measurement not selected!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(Settings.this,selectedUnit.getText(), Toast.LENGTH_SHORT).show();
                    try
                    {

                        if(metric.isChecked())
                        {
                            unitsMeasured = "Metric";
                        }
                        else {
                            unitsMeasured = "Imperial";
                        }

                        //speedCameras = showCameras.toString();
                        //speedLimits = showLimits.toString();
                        //traffic = showLimits.toString();
                        //roadConstruction = showLimits.toString();

                        userSettings = new UserSettings(unitsMeasured, speedCameras, speedLimits, traffic, roadConstruction);

                        DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());
                        myRef.child("Settings").setValue(userSettings)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Settings.this, "Settings saved successfully", Toast.LENGTH_SHORT).show();
                                        //Intent openNewActivity = new Intent(Settings.this, HomeScreen.class);
                                        //openNewActivity.putExtra("UnitsMeasured", unitsMeasured);
                                        //startActivity(openNewActivity);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Toast.makeText(Settings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(Settings.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

     /*
    public void onclickbuttonMethod(View v)
    {
        int selectedId = unitOfMeasurement.getCheckedRadioButtonId();
        selectedUnit = (RadioButton) findViewById(selectedId);
        if(selectedId==-1){
            Toast.makeText(Settings.this,"Unit of measurement not selected!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Settings.this,selectedUnit.getText(), Toast.LENGTH_SHORT).show();
        }

    }
     */

   // @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    //@Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}