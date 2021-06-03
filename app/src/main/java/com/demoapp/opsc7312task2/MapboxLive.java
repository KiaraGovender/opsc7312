package com.demoapp.opsc7312task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// classes needed to initialize map
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MapboxLive extends AppCompatActivity implements OnMapReadyCallback
{
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_mapbox_live);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    // mapbox methods
    @Override
    protected void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41"));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    // methods end
}