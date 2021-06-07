package com.demoapp.opsc7312task2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouritesItemAdapter extends RecyclerView.Adapter<FavouritesItemAdapter.ItemsViewHolder> {

    private ArrayList<UserFavorites> userFavoritesArrayList;
    private Context context;

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favouriteitem, parent, false);
        ItemsViewHolder pvh = new ItemsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        UserFavorites currentUserFavourite = userFavoritesArrayList.get(position);

        holder.name.setText(currentUserFavourite.locationName);
        holder.lat.setText(currentUserFavourite.latitude.toString());
        holder.lng.setText(currentUserFavourite.longitude.toString());
    }

    @Override
    public int getItemCount() {
        return userFavoritesArrayList.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        public TextView name, lat, lng;
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txvName);
            lat = itemView.findViewById(R.id.txvLat);
            lng = itemView.findViewById(R.id.txvLng);
        }
    }

    public FavouritesItemAdapter(ArrayList<UserFavorites> FavouritesList, Context context)
    {
        userFavoritesArrayList = FavouritesList;
        this.context = context;
    }
}
