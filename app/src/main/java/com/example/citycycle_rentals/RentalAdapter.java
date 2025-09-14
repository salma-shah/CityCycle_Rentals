package com.example.citycycle_rentals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.RentalViewHolder>{
    private List<Rental> rentalList;
    private Context context;
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private Bike bike;

    public RentalAdapter(Context context, List<Rental> rentalList) {
        this.context = context;
        this.rentalList = rentalList;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public RentalAdapter.RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rental_item, parent, false);
        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalAdapter.RentalViewHolder holder, int position) {
        Rental rental = rentalList.get(position);
        Bike bike = rental.getBike();

        // getting bike details of the rental
        holder.tvBikeName.setText(bike.getName());
        holder.tvStationLocation.setText(bike.getLocation());
        holder.tvCategory.setText(bike.getCategory());
        // rental details
        holder.tvRentalId.setText(String.valueOf(rental.getRentalId()));
        holder.tvDuration.setText(rental.getDuration());

        // we will only display date
        String startTime = rental.getStartDate();
        String startDetails[] = startTime.split(" ");
        String startDateOnly = startDetails[0];
        holder.tvRentalStartDate.setText(startDateOnly);
        String endTime = rental.getEndDate();
        String endDetails[] = endTime.split(" ");
        String endDateOnly = endDetails[0];
        holder.tvRentalEndDate.setText(endDateOnly);
        // bike image
        if (bike.getBikeImage() != 0) {
            int image = bike.getBikeImage();
            holder.imgBikeImage.setImageResource(image);
        } else {
            holder.imgBikeImage.setImageResource(R.drawable.default_bike);
        }

        holder.tvDurationTimer.setVisibility(View.GONE); // this will only be visible if rental is ongoing

        // setting up timer
        SimpleDateFormat endRentalTime = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.US);
        // calculating the duration
        try {
            Date endDate = endRentalTime.parse(rental.getEndDate());
            Date currentDate = new Date();
            // remaining time of the duration showing in the timer
            long remainingRentalTime = endDate.getTime() - currentDate.getTime();
            if (remainingRentalTime > 0) {
                new CountDownTimer(remainingRentalTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        NumberFormat f = new DecimalFormat("00");// now days, hours, mins, seconds will be in 2 digit format only
                        long day = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                        long hour = (millisUntilFinished / 3600000) % 24;
                        long min = (millisUntilFinished / 60000) % 60;
                        long sec = (millisUntilFinished / 1000) % 60;

                        if (day > 0) {
                            // formating the timer
                            holder.tvDurationTimer.setText(day + " d " +f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                        } else {
                            holder.tvDurationTimer.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                        }
                    }
                    @Override
                    public void onFinish() {
                        holder.tvDurationTimer.setText("00:00:00"); // once over, go to 00
                       // holder.tvDurationTimer.setTextColor(Color.RED);
                    }
                }.start();
                } else
                    {
                        holder.tvDurationTimer.setText("00:00:00");
                    }

                } catch(ParseException e){
                    throw new RuntimeException(e);
                }

        // if rental is still going, set rental button to be visible because user needs to end the rental
        if (rental.getStatus().equals("Ongoing")) {
            holder.tvDurationTimer.setVisibility(View.VISIBLE);
            holder.btnEndRental.setVisibility(View.VISIBLE);
            holder.btnEndRental.setOnClickListener(v -> endRental(rental.getRentalId(), position));
        }
        else {
            holder.btnEndRental.setVisibility(View.GONE);
        }
        // if bike has not been returned yet, user needs to check in the bike
        if (bike.getStatus().equals("Rented")) {
            holder.btnCheckInBike.setVisibility(View.VISIBLE);
            holder.btnCheckInBike.setOnClickListener(v ->  checkInBike(rental.getBikeId(), bike));
        }
        else {
            holder.btnCheckInBike.setVisibility(View.GONE);
        }
    }

    private void checkInBike(int bikeId, Bike bike) {

        if (bikeId != -1) {
            boolean bikeStatusUpdated = dbHelper.updateBikeStatus(bikeId, "Available");
            // changing bike's status back to available since it is no longer rented
            if (bikeStatusUpdated) {
                Toast.makeText(context, bike.getName() + " was checked in successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void endRental(int rentalId, int position) {
        Rental rental = rentalList.get(position);
        if (rentalId != -1) {
            // updating rental status
            boolean rentalStatusUpdated = dbHelper.updateRentalStatus(rentalId, "Completed");  // rental is over
            if (rentalStatusUpdated) {
                notifyItemChanged(position);
                Toast.makeText(context, "Your rental has ended!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount()
    {
        Log.d("RentalAdapter", "Item count: " + rentalList.size());
        return rentalList.size();
    }
    public void updateList(List<Rental> newList) {
        Log.d("RentalAdapter", "Updating list, new size: " + newList.size());
        this.rentalList = newList;
        notifyDataSetChanged();
    }

    public class RentalViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBikeImage;
        TextView tvRentalId, tvBikeName, tvStationLocation, tvCategory, tvRentalStartDate, tvDuration, tvDurationTimer, tvRentalEndDate;
        Button btnEndRental, btnCheckInBike;
        public RentalViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEndRental = itemView.findViewById(R.id.btnEndRental);
            btnCheckInBike = itemView.findViewById(R.id.btnCheckInBike);
            imgBikeImage = itemView.findViewById(R.id.imgBikeImage);
            tvBikeName = itemView.findViewById(R.id.tvBikeName);
            tvRentalId = itemView.findViewById(R.id.tvRentalId);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvStationLocation = itemView.findViewById(R.id.tvStationLocation);
            tvRentalStartDate = itemView.findViewById(R.id.tvRentalStartDate);
            tvRentalEndDate = itemView.findViewById(R.id.tvRentalEndDate);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDurationTimer = itemView.findViewById(R.id.tvDurationTimer);
        }
    }
}

