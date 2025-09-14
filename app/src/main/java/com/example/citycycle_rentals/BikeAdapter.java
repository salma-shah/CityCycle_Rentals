package com.example.citycycle_rentals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.BikeViewHolder> {
    private List<Bike> bikeList;
    public BikeAdapter(List<Bike> bikeList) {
        this.bikeList = bikeList;
    }

    @NonNull
    @Override
    public BikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bike_item, parent, false);
        return new BikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeAdapter.BikeViewHolder holder, int position) {
        Bike bike = bikeList.get(position);
        holder.tvBikeName.setText(bike.getName());
        holder.tvBikeLocation.setText(bike.getLocation());
        holder.tvBikeCategory.setText(bike.getCategory());
        holder.tvBikeHourPrice.setText("LKR " + bike.getPriceHour());

        // ensuring status appears, only when bike is currently rented
        String bikeStatus = bike.getStatus();
        if (bikeStatus.equals("Rented")) {
            holder.tvBikeStatus.setVisibility(View.VISIBLE);
            holder.tvBikeStatus.setText("Unavailable for rent");
        }
        else {
            holder.tvBikeStatus.setVisibility(View.GONE);
        }

        if (bike.getBikeImage() != 0) {
            int image = bike.getBikeImage();
            holder.imgBikeImage.setImageResource(image);
        } else {
            // set a default image if bikeImage is not present
            holder.imgBikeImage.setImageResource(R.drawable.baseline_add_a_photo_24);
        }

        holder.btnViewBike.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BikeDetailsActivity.class);
            // passing the bike id of selected bike onto the next page
            intent.putExtra("bike_id", bike.getBikeId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount()
    {
        Log.d("BikeAdapter", "Item count: " + bikeList.size());
        return bikeList.size();
    }
    public void updateList(List<Bike> newList) {
        Log.d("BikeAdapter", "Updating list, new size: " + newList.size());
        this.bikeList = newList;
        notifyDataSetChanged();
    }

    public class BikeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBikeImage;
        TextView tvBikeName, tvBikeLocation, tvBikeCategory, tvBikeHourPrice, tvBikeStatus;
        Button btnViewBike;
        public BikeViewHolder(@NonNull View itemView) {
            super(itemView);
            btnViewBike = itemView.findViewById(R.id.btnViewBike);
            imgBikeImage = itemView.findViewById(R.id.imgBikeImage);
            tvBikeName = itemView.findViewById(R.id.tvBikeName);
            tvBikeCategory = itemView.findViewById(R.id.tvBikeCategory);
            tvBikeLocation = itemView.findViewById(R.id.tvBikeLocation);
            tvBikeHourPrice = itemView.findViewById(R.id.tvBikeHourPrice);
            tvBikeStatus = itemView.findViewById(R.id.tvBikeStatus);
        }
    }
}
