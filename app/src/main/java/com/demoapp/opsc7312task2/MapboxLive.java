package com.demoapp.opsc7312task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.search.CategorySearchEngine;
import com.mapbox.search.CategorySearchOptions;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.location.DefaultLocationProvider;
import com.mapbox.search.result.SearchResult;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.View;
import android.widget.Button;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;

public class MapboxLive extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener, FavouritesDialog.FavouriteDialogListener
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    UserSettings userSettings;
    String directionsCriteria, selectedLandmark;
    boolean trafficSetting;

    UserFavorites userFavorites;
    UserLandmarks userLandmarks;

    private MapView mapView;
    private MapboxMap map;
    private Button startButton;
    private FloatingActionButton favButton;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private Double lat = 0.0;
    private Double lng = 0.0;
    private String fav;

    private static final int PLACE_SELECTION_REQUEST_CODE = 56789;

    private CategorySearchEngine categorySearchEngine;
    private SearchRequestTask searchRequestTask;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_mapbox_live);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());


        //MapboxSearchSdk.initialize(this.getApplication(), getString(R.string.access_token),
              //  new DefaultLocationProvider(this.getApplication()));

        categorySearchEngine = MapboxSearchSdk.createCategorySearchEngine();

        final CategorySearchOptions options = new CategorySearchOptions.Builder()
                .limit(5)
                .build();

        searchRequestTask = categorySearchEngine.search("Food", options, searchCallback);

        // favourites button
        favButton = findViewById(R.id.btnFavourites);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


        // start navigation button
        startButton = findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean simulateRoute = true;
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(simulateRoute)
                        .build();
                NavigationLauncher.startNavigation(MapboxLive.this, options);
            }
        });

        // checking database if user settings exist
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
                            directionsCriteria = "METRIC";
                        }
                        if (userSettings.getUnitSetting().equals("Imperial"))
                        {
                            directionsCriteria = "IMPERIAL";
                        }

                        if (userSettings.getTraffic().equals("true"))
                        {
                            trafficSetting = true;
                        }
                        else
                        {
                            trafficSetting = false;
                        }


                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(MapboxLive.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    //If unitSetting has not been selected
                    directionsCriteria = "METRIC";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(MapboxLive.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        myRef.child("Landmarks").addValueEventListener(new ValueEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userLandmarks = snapshot.getValue(UserLandmarks.class);

                if (userLandmarks != null)
                {
                    selectedLandmark = userLandmarks.getPreferredLandmark();

                    try
                    {
                        if(selectedLandmark != null && !selectedLandmark.equals("All"))
                         {
                            searchRequestTask = categorySearchEngine.search(selectedLandmark, options, searchCallback);
                         }
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(MapboxLive.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(MapboxLive.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openDialog(){
        FavouritesDialog favouritesDialog = new FavouritesDialog();
        favouritesDialog.show(getSupportFragmentManager(),"favourite dialog");
    }

    public void applyTexts(String favourite) {
        fav = favourite;
        try
        {
            userFavorites = new UserFavorites(fav, lat, lng);
            DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());
            myRef.child("Favourites").push().setValue(userFavorites)
                    .addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(MapboxLive.this, "New Favourite Location successfully saved", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(MapboxLive.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        catch (Exception ex)
        {
            Toast.makeText(MapboxLive.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
        searchRequestTask.cancel();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    // initialising map
    /** CODE ATTRIBUTION
     *  Build a navigation app for Android, mapbox.com.
     * https://docs.mapbox.com/help/tutorials/android-navigation-sdk/
     * **/
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MapboxLive.this.map = mapboxMap;

       mapboxMap.setStyle(Style.MAPBOX_STREETS,
                 new Style.OnStyleLoaded() {
                     @Override
                     public void onStyleLoaded(@NonNull Style style) {
                         enableLocationComponent(style);
                         addDestinationIconSymbolLayer(style);

                         mapboxMap.addOnMapClickListener(MapboxLive.this);

                         /** CODE ATTRIBUTION
                          *  Traffic, mapbox.com.
                          * https://docs.mapbox.com/android/plugins/guides/traffic/
                          * **/
                         // adding a real time traffic layer to the map
                         TrafficPlugin trafficPlugin = new TrafficPlugin(mapView, mapboxMap, style);
                         trafficPlugin.setVisibility(trafficSetting);

                     }
                 });
    }

    /** CODE ATTRIBUTION
     *  Build a navigation app for Android, mapbox.com.
     * https://docs.mapbox.com/help/tutorials/android-navigation-sdk/
     * **/
    // add marker on click
    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);

    }

    /** CODE ATTRIBUTION
     *  Build a navigation app for Android, mapbox.com.
     * https://docs.mapbox.com/help/tutorials/android-navigation-sdk/
     * **/
    // get user location
    @SuppressLint("MissingPermission")
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationComponent = map.getLocationComponent();;
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(map.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /** CODE ATTRIBUTION
     *  Build a navigation app for Android, mapbox.com.
     * https://docs.mapbox.com/help/tutorials/android-navigation-sdk/
     * **/
    @Override
    // adding a marker to the map
    public boolean onMapClick(@NonNull LatLng point) {
        Intent data = new Intent();
        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());


        lat = destinationPoint.latitude();
        lng = destinationPoint.longitude();

        GeoJsonSource source = map.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        getRoute(originPoint, destinationPoint);

        startButton.setEnabled(true);
        startButton.setBackgroundResource(R.color.mapboxBlue);
        favButton.setEnabled(true);
        favButton.setBackgroundResource(R.color.mapbox_plugins_green);

        /** CODE ATTRIBUTION
         *  Places plugin for Android, mapbox.com.
         * https://docs.mapbox.com/android/plugins/guides/places/
         * **/
        Intent intent = new PlacePicker.IntentBuilder()
                .accessToken(Mapbox.getAccessToken())
                .placeOptions(
                        PlacePickerOptions.builder()
                                .statingCameraPosition(
                                        new CameraPosition.Builder()
                                                .target(new LatLng(destinationPoint.latitude(),destinationPoint.longitude()))
                                                .zoom(25)
                                                .build())
                                .build())
                .build(this);
        startActivityForResult(intent, PLACE_SELECTION_REQUEST_CODE);

        return true;
    }

    /** CODE ATTRIBUTION
     *  Places plugin for Android, mapbox.com.
     * https://docs.mapbox.com/android/plugins/guides/places/
     * **/
    // retrieving information about user's location
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_SELECTION_REQUEST_CODE && resultCode == RESULT_OK){

            // Retrieve the information from the selected location's CarmenFeature

            CarmenFeature carmenFeature = PlacePicker.getPlace(data);
        }
    }

    /** CODE ATTRIBUTION
     *  Build a navigation app for Android, mapbox.com.
     * https://docs.mapbox.com/help/tutorials/android-navigation-sdk/
     * **/
    // method to get route (calculates best route from user destination to marker)
    private void getRoute(Point origin, Point destination) {
        // use the metric measurement system
        if(directionsCriteria == "METRIC")
        {
            NavigationRoute.builder(this)
                    .accessToken(Mapbox.getAccessToken())
                    .voiceUnits(DirectionsCriteria.METRIC)
                    .origin(origin)
                    .destination(destination)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>()
                    {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            Log.d(TAG, "Response code: " + response.code());
                            if (response.body() == null) {
                                Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Log.e(TAG, "No routes found");
                                return;
                            }

                            currentRoute = response.body().routes().get(0);

                            // Draw the route on the map
                            if (navigationMapRoute != null) {
                                navigationMapRoute.removeRoute();
                            } else {
                                navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
                            }
                            navigationMapRoute.addRoute(currentRoute);
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                            Log.e(TAG, "Error: " + throwable.getMessage());
                        }
                    });
        }
        // using other (IMPERIAL) measurement system
        else
        {
            NavigationRoute.builder(this)
                    .accessToken(Mapbox.getAccessToken())
                    .voiceUnits(DirectionsCriteria.IMPERIAL)
                    .origin(origin)
                    .destination(destination)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>()
                    {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            Log.d(TAG, "Response code: " + response.code());
                            if (response.body() == null) {
                                Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Log.e(TAG, "No routes found");
                                return;
                            }

                            currentRoute = response.body().routes().get(0);

                            // Draw the route on the map
                            if (navigationMapRoute != null) {
                                navigationMapRoute.removeRoute();
                            } else {
                                navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
                            }
                            navigationMapRoute.addRoute(currentRoute);
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                            Log.e(TAG, "Error: " + throwable.getMessage());
                        }
                    });
        }
    }

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




    // methods end
}