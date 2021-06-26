package com.demoapp.opsc7312task2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.search.CategorySearchEngine;
import com.mapbox.search.CategorySearchOptions;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.location.DefaultLocationProvider;
import com.mapbox.search.result.SearchResult;

import java.util.List;

public class Landmarks extends AppCompatActivity
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    String selectedLandmark;
    RadioGroup radioGroupLandmarks;
    RadioButton all, ATM, cafe, food, fitnessCenter, grocery, hotel, hospital, parking, police, restaurant, school, selectedLandmarkUnit;
    Button save;
    UserLandmarks userLandmarks;

    private CategorySearchEngine categorySearchEngine;
    private SearchRequestTask searchRequestTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmarks);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());

        radioGroupLandmarks = findViewById(R.id.radioGroupLandmarks);
        all = findViewById(R.id.radioAll);
        ATM = findViewById(R.id.radioATM);
        cafe = findViewById(R.id.radioCafe);
        food = findViewById(R.id.radioFood);
        fitnessCenter = findViewById(R.id.radioFitnessCenter);
        grocery = findViewById(R.id.radioGrocery);
        hotel = findViewById(R.id.radioHotel);
        hospital = findViewById(R.id.radioHospital);
        parking = findViewById(R.id.radioParking);
        police = findViewById(R.id.radioPolice);
        restaurant = findViewById(R.id.radioRestaurant);
        school = findViewById(R.id.radioSchool);

        save = findViewById(R.id.btnSaveLandmarkSetting);

        //MapboxSearchSdk.initialize(this.getApplication(), getString(R.string.access_token),
              //  new DefaultLocationProvider(this.getApplication()));

        /*
        categorySearchEngine = MapboxSearchSdk.createCategorySearchEngine();

        final CategorySearchOptions options = new CategorySearchOptions.Builder()
                .limit(5)
                .build();

       // searchRequestTask = categorySearchEngine.search(selectedLandmark, options, searchCallback);

         */

        myRef.child("Landmarks").addValueEventListener(new ValueEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userLandmarks = snapshot.getValue(UserLandmarks.class);

                if (userLandmarks != null)
                {
                    try
                    {
                        switch (userLandmarks.getPreferredLandmark())
                        {
                            case "All":
                                all.setChecked(true);
                                break;

                            case "ATM":
                                ATM.setChecked(true);
                                break;

                            case "Cafe":
                                cafe.setChecked(true);
                                break;

                            case "Food":
                                food.setChecked(true);
                                break;

                            case "Fitness Center":
                                fitnessCenter.setChecked(true);
                                break;

                            case "Grocery":
                                grocery.setChecked(true);
                                break;

                            case "Hotel":
                                hotel.setChecked(true);
                                break;

                            case "Hospital":
                                hospital.setChecked(true);
                                break;

                            case "Parking":
                                parking.setChecked(true);
                                break;

                            case "Police":
                                police.setChecked(true);
                                break;

                            case "Restaurant":
                                restaurant.setChecked(true);
                                break;

                            case "School":
                                school.setChecked(true);
                                break;
                        }

                        /**if(selectedLandmark != null && !selectedLandmark.equals("All"))
                        {
                            searchRequestTask = categorySearchEngine.search(selectedLandmark, options, searchCallback);
                        }**/

                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(Landmarks.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Landmarks.this, "Please save your selection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Landmarks.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int selectedId = radioGroupLandmarks.getCheckedRadioButtonId();
                selectedLandmarkUnit = (RadioButton) findViewById(selectedId);
                if(selectedId==-1)
                {
                    Toast.makeText(Landmarks.this,"Preferred Landmark not selected!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(Landmarks.this,selectedLandmarkUnit.getText(), Toast.LENGTH_SHORT).show();
                    try
                    {
                        if(all.isChecked())
                        {
                            selectedLandmark = "All";
                        }
                        else if (ATM.isChecked())
                        {
                            selectedLandmark= "ATM";
                        }
                        else if (cafe.isChecked())
                        {
                            selectedLandmark= "Cafe";
                        }
                        else if (food.isChecked())
                        {
                            selectedLandmark= "Food";
                        }
                        else if (fitnessCenter.isChecked())
                        {
                            selectedLandmark= "Fitness Center";
                        }
                        else if (grocery.isChecked())
                        {
                            selectedLandmark= "Grocery";
                        }
                        else if (hotel.isChecked())
                        {
                            selectedLandmark= "Hotel";
                        }
                        else if (hospital.isChecked())
                        {
                            selectedLandmark= "Hospital";
                        }
                        else if (parking.isChecked())
                        {
                            selectedLandmark= "Parking";
                        }
                        else if (police.isChecked())
                        {
                            selectedLandmark= "Police";
                        }
                        else if (restaurant.isChecked())
                        {
                            selectedLandmark= "Restaurant";
                        }
                        else if (school.isChecked())
                        {
                            selectedLandmark= "School";
                        }

                        userLandmarks = new UserLandmarks(selectedLandmark);

                        DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());
                        myRef.child("Landmarks").setValue(userLandmarks)
                                .addOnSuccessListener(new OnSuccessListener<Void>()
                                {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Toast.makeText(Landmarks.this, "Preferred Landmark saved successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Toast.makeText(Landmarks.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(Landmarks.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        /*
        if(selectedLandmark != null && !selectedLandmark.equals("All"))
        {
            searchRequestTask = categorySearchEngine.search(selectedLandmark, options, searchCallback);
        }

         */

    }


    /*
    private final SearchCallback searchCallback = new SearchCallback()
    {
        @Override
        public void onResults(@NonNull List<? extends SearchResult> results, @NonNull ResponseInfo responseInfo)
        {
            if (results.isEmpty())
            {
                Log.i("SearchApiExample", "No category search results");
            } else {
                Log.i("SearchApiExample", "Category search results: " + results);
            }
        }

        @Override
        public void onError(@NonNull Exception e)
        {
            Log.i("SearchApiExample", "Search error", e);
        }
    };

    @Override
    public void onDestroy()
    {
        //searchRequestTask.cancel();
        super.onDestroy();
    }

     */
}