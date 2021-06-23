package com.demoapp.opsc7312task2;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.geojson.Point;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ServiceProvider;
import com.mapbox.search.location.DefaultLocationProvider;
import com.mapbox.search.record.FavoriteRecord;
import com.mapbox.search.record.FavoritesDataProvider;
import com.mapbox.search.record.IndexableDataProvider.CompletionCallback;
import com.mapbox.search.record.LocalDataProvider.OnDataChangedListener;
import com.mapbox.search.result.SearchAddress;
import com.mapbox.search.result.SearchResultType;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import kotlin.Unit;

public class Landmarks extends AppCompatActivity
{
    private FavoritesDataProvider favoritesDataProvider;
    //private final FavoritesDataProvider favoritesDataProvider = MapboxSearchSdk.serviceProvider.favoritesDataProvider();

    private Future<?> futureTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_landmarks);
        MapboxSearchSdk.initialize(this.getApplication(), getString(R.string.access_token),
                new DefaultLocationProvider(this.getApplication()));

        favoritesDataProvider = MapboxSearchSdk.getServiceProvider().favoritesDataProvider();
        favoritesDataProvider.addOnDataChangedListener(onDataChangedListener);

        final FavoriteRecord newFavorite = new FavoriteRecord(
                UUID.randomUUID().toString(),
                "Paris Eiffel Tower",
                "Eiffel Tower, Paris, France",
                new SearchAddress(null, null, null, null, null, "Paris", null, null, "France"),
                null,
                null,
                null,
                Point.fromLngLat(2.294434, 48.858349),
                SearchResultType.PLACE,
                null
        );

        futureTask = favoritesDataProvider.add(newFavorite, addFavoriteCallback);
    }

    private final CompletionCallback<List<FavoriteRecord>> retrieveFavoritesCallback = new CompletionCallback<List<FavoriteRecord>>() {
        @Override
        public void onComplete(List<FavoriteRecord> result)
        {
            Log.i("SearchApiExample", "Favorite records: " + result);
        }

        @Override
        public void onError(@NotNull Exception e)
        {
            Log.i("SearchApiExample", "Unable to retrieve favorite records", e);
        }
    };

    private final CompletionCallback<Unit> addFavoriteCallback = new CompletionCallback<Unit>()
    {
        @Override
        public void onComplete(Unit result)
        {
            Log.i("SearchApiExample", "Favorite record added");
            futureTask = favoritesDataProvider.getAll(retrieveFavoritesCallback);
        }

        @Override
        public void onError(@NotNull Exception e)
        {
            Log.i("SearchApiExample", "Unable to add a new favorite record", e);
        }
    };

    private final OnDataChangedListener<FavoriteRecord> onDataChangedListener = newData ->
    {
        Log.i("SearchApiExample", "Favorites data changed. New data: " + newData);
    };




    @Override
    protected void onDestroy()
    {
        favoritesDataProvider.removeOnDataChangedListener(onDataChangedListener);
        futureTask.cancel(true);
        super.onDestroy();
    }
}