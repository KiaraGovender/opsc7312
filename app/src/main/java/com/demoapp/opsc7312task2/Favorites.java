package com.demoapp.opsc7312task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Favorites extends AppCompatActivity
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    UserFavorites userFavorites, items;

    String locationName;
    Double locationLatitude, locationLongitude;

    List<UserFavorites> userFavoritesList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        DatabaseReference myRef = database.getReference(mAuth.getCurrentUser().getUid());

        final ArrayList<UserFavorites> FavouriteItemsArrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);

        myRef.child("Favourites").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userFavorites = new UserFavorites();
                for (DataSnapshot favouriteValues : snapshot.getChildren())
                {
                    userFavorites = favouriteValues.getValue(UserFavorites.class);
                    assert userFavorites != null;
                    items = new UserFavorites(userFavorites.locationName, userFavorites.latitude, userFavorites.longitude);
                    FavouriteItemsArrayList.add(items);
                }

                try
                {
                    recyclerView = findViewById(R.id.recycler1);
                    recyclerView.setHasFixedSize(true);
                    Collections.reverse(FavouriteItemsArrayList);
                    adapter = new FavouritesItemAdapter(FavouriteItemsArrayList, Favorites.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                }
                catch (Exception ex)
                {
                    Toast.makeText(Favorites.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Favorites.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}