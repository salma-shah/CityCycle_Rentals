package com.example.citycycle_rentals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RentalActivity extends AppCompatActivity {
    Button btnRentBike;
    private int bikeID;
    TextView tvCustomerEmail, tvCustomerName, tvCustomerContact, tvBikeName, tvBikeLocation, tvRental, tvRentalStartDate, tvRentalEndDate, tvRentalEndTime, tvRentalStartTime;
    ImageView imgBikeImage;
    Spinner spinnerRental;
    private DBHelper dbHelper;
    private Rental rental;
    private Bike bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rental);

        // initializing
        dbHelper = new DBHelper(this);
        tvRental = findViewById(R.id.tvRental);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerContact = findViewById(R.id.tvCustomerContact);
        tvBikeLocation = findViewById(R.id.tvBikeLocation);
        tvRentalEndDate = findViewById(R.id.tvRentalEndDate);
        tvRentalStartDate = findViewById(R.id.tvRentalStartDate);
        tvBikeName = findViewById(R.id.tvBikeName);
        imgBikeImage = findViewById(R.id.imgBikeImage);
        spinnerRental = findViewById(R.id.spinnerRental);
        btnRentBike = findViewById(R.id.btnRentBike);
        tvRentalStartTime = findViewById(R.id.tvRentalStartTime);
        tvRentalEndTime = findViewById(R.id.tvRentalEndTime);

        // using shared prefernces to get user details through their email
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        if (userEmail != null) {
            User user = dbHelper.getUserImportantDetails(userEmail);

            if (user != null) {
                tvCustomerName.setText(user.getFirstName() + " " + user.getLastName());
                tvCustomerContact.setText(user.getContact());
                tvCustomerEmail.setText(user.getEmail());

            } else {
                tvCustomerName.setText("No First Name");
                tvCustomerContact.setText("No Contact");
                tvCustomerEmail.setText("No Email");
            }
        }

        // passed from previous activity
            int bikeID = getIntent().getIntExtra("bike_id", -1);

            if (bikeID != -1) {
                bike = dbHelper.getBikeImportantDetails(bikeID);
                this.bikeID = bikeID;
                // logging
                Log.d("BikeID: ", "The Bike ID which is passed on is: " + bikeID);

                if (bike != null) {
                    tvBikeName.setText(bike.getName());
                    tvBikeLocation.setText(bike.getLocation());
                    if (bike.getBikeImage() != 0){
                        int image = bike.getBikeImage();
                        imgBikeImage.setImageResource(image);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bike.getBikeImage(), 0, bike.getBikeImage().length);
//            holder.imgBikeImage.setImageBitmap(bitmap);
                    }
                    else {
                        imgBikeImage.setImageResource(R.drawable.default_bike);
                    }
                } else {
                    tvBikeName.setText("No Bike Name");
                    tvBikeLocation.setText("No Location");
                    imgBikeImage.setImageResource(R.drawable.baseline_person_24);
                }
            } else {
                Toast.makeText(this, "Invalid Bike ID", Toast.LENGTH_SHORT).show();
            }

            btnRentBike.setVisibility(View.GONE);

            // set duration into text view
            spinnerRental.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // selected rental duration
                    String selectedRental = parentView.getItemAtPosition(position).toString();
                    // display it
                    tvRental.setText(selectedRental);

                    if (position > 0) {
                        // date and time format
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                        // rental start date
                        String rentalStartDate = simpleDateFormat.format(cal.getTime());
                        String rentalStartTime = timeFormat.format(cal.getTime());
                        tvRentalStartDate.setText(rentalStartDate);
                        tvRentalStartTime.setText(rentalStartTime);

                        btnRentBike.setVisibility(View.VISIBLE);

                        // lets calculate end date and timing, based on hours and days
                        int rentalDays = 0;
                        int rentalHours = 0;
                                // conditions
                        if (selectedRental.contains("1 hour")) {
                            rentalHours= 1;
                        }
                        if (selectedRental.contains("2 hours")) {
                            rentalHours= 2;
                        }
                        if (selectedRental.contains("3 hours")) {
                            rentalHours= 3;
                        }
                        if (selectedRental.contains("4 hours")) {
                            rentalHours= 4;
                        }
                        if (selectedRental.contains("5 hours")) {
                            rentalHours= 5;
                        }
                        if (selectedRental.contains("6 hours")) {
                            rentalHours= 6;
                        }
                        if (selectedRental.contains("1 day")) {
                            rentalDays= 1;
                        }
                        if (selectedRental.contains("1 week")) {
                            rentalDays= 7;
                        }
                        if (selectedRental.contains("10 days")) {
                            rentalDays= 10;
                        }
                        // for rental duration that lasts days
                        if (rentalDays > 0) {
                            cal.add(Calendar.DAY_OF_MONTH, rentalDays);
                            String rentalEndDate = simpleDateFormat.format(cal.getTime());
                            String rentalEndTime = timeFormat.format(cal.getTime());
                            // set the fields
                            tvRentalEndDate.setText(rentalEndDate);
                            tvRentalEndTime.setText(rentalEndTime);
                        }
                        else {
                            // rental duration of hours
                            cal.add(Calendar.HOUR_OF_DAY, rentalHours);
                            String rentalEndDate = simpleDateFormat.format(cal.getTime());
                            String rentalEndTime = timeFormat.format(cal.getTime());
                            // set the fields
                            tvRentalEndDate.setText(rentalEndDate);
                            tvRentalEndTime.setText(rentalEndTime);
                        }
                    }
                    else {
                        btnRentBike.setVisibility(View.GONE);
                        tvRentalStartDate.setText("");
                        tvRentalEndDate.setText("");
                        tvRentalEndTime.setText("");
                        tvRentalStartTime.setText("");
                    }
                    }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    tvRental.setText("No duration selected");
                    tvRentalStartDate.setText("");
                    tvRentalEndDate.setText("");
                }
            });

            btnRentBike.setOnClickListener(v-> confirmRental());

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

    private void confirmRental() {

        Log.d("BikeID: ", "The Bike ID which is passed on is: " + bikeID);

        String userEmail = tvCustomerEmail.getText().toString();
        int bikeId = this.bikeID;
        String startDate = tvRentalStartDate.getText().toString() + " " + tvRentalStartTime.getText().toString();
        String endDate = tvRentalEndDate.getText().toString() + " " + tvRentalEndTime.getText().toString();
        String duration = tvRental.getText().toString();
        String status = "Ongoing";

        // checking is existing card exists for that email already
        User existingCard = dbHelper.getCardDetails(userEmail);

        if (existingCard != null) {
            // adding rental
            long result = dbHelper.addRental(userEmail, bikeId, startDate, endDate, duration, status);

            if (result != -1) {
                // updates bike status since it is rented now
                boolean bikeStatusUpdated = dbHelper.updateBikeStatus(bikeId, "Rented");
                if (bikeStatusUpdated) {
                    Toast.makeText(this, bike.getName() + " has been successfully rented!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Your Rental ID is: " + result, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Your rental history has been updated!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RentalActivity.this, NavigationBar.class));
                }
            }
        }
        else {
            Toast.makeText(this, "You need to enter your card details first.", Toast.LENGTH_SHORT).show();
        }

        }

}