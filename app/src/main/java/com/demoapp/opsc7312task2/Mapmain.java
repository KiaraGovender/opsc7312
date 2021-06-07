package com.demoapp.opsc7312task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;

public class Mapmain extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    IntentHelper helper;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggleOnAndOff;
    NavigationView navigationView;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapmain);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());

        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.nav_Drawer);
        helper = new IntentHelper();

        toggleOnAndOff = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggleOnAndOff);
        toggleOnAndOff.syncState();



    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view)
    {
        //To do code
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_MapMain:
                helper.openIntent(this, Mapmain.class);
                break;

            case R.id.nav_MapBox:
                helper.openIntent(this, MapboxLive.class);
                break;

            case R.id.nav_Landmarks:
                helper.openIntent(this, Landmarks.class);
                //Toast.makeText(this, "Feature in development!",
                        //Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Favorites:
                helper.openIntent(this, Favorites.class);
                //Toast.makeText(this, "In development!",
                        //Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Search:
                helper.openIntent(this, Search.class);
                //Toast.makeText(this, "Downloaded Maps still in development!",
                //Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_DownloadedMaps:
                helper.openIntent(this, DownloadedMaps.class);
                //Toast.makeText(this, "Downloaded Maps still in development!",
                        //Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Settings:
                helper.openIntent(this, Settings.class);
                //Toast.makeText(this, "Settings still in development!",
                        //Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Logout:
                Toast.makeText(this, "You have successfully logged out!",
                        Toast.LENGTH_SHORT).show();

                mAuth.signOut();
                finishAffinity();
                System.exit(0);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        //To do code
    }
}