package com.example.citycycle_rentals;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {
    TextView tvCustomerEmail, tvCustomerName, tvCustomerContact, tvBikeName, tvBikeLocation, tvReservation, tvReservationDuration;
    Button btnConfirmReservation;
    EditText etDob;
    Spinner spinnerReservation;
    ImageView imgBikeImage;
    private DBHelper dbHelper;
    private Bike bike;
    private int bikeID;
    private ArrayList<String> availableTimesList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    private boolean isReservationConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation);

        dbHelper = new DBHelper(this);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerContact = findViewById(R.id.tvCustomerContact);
        etDob = findViewById(R.id.etDob);
        tvBikeLocation = findViewById(R.id.tvBikeLocation);
        tvBikeName = findViewById(R.id.tvBikeName);
        tvReservationDuration = findViewById(R.id.tvReservationDuration);
        imgBikeImage = findViewById(R.id.imgBikeImage);
        tvReservation = findViewById(R.id.tvReservation);
        btnConfirmReservation = findViewById(R.id.btnConfirmReservation);
        spinnerReservation = findViewById(R.id.spinnerReservation);

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

            int bikeID = getIntent().getIntExtra("bike_id", -1);

            if (bikeID != -1) {
                bike = dbHelper.getBikeImportantDetails(bikeID);
                this.bikeID = bikeID;

                if (bike != null) {
                    // setting fields
                    tvBikeName.setText(bike.getName());
                    tvBikeLocation.setText(bike.getLocation());

                    // set bike image if it is available
                    if (bike.getBikeImage() != 0) {
                        int image = bike.getBikeImage();
                        imgBikeImage.setImageResource(image);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bike.getBikeImage(), 0, bike.getBikeImage().length);
//            holder.imgBikeImage.setImageBitmap(bitmap);
                    } else {
                        // otherwise default
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
            // this is to show calendar when you need to choose pick up date
            etDob.setOnClickListener(view -> showDatePicker());

            btnConfirmReservation.setVisibility(View.GONE);

            // converting string array into list so selected timings can be removed
           String availableTimesArray[] = getResources().getStringArray(R.array.reservation_availability);
           availableTimesList = new ArrayList<>(Arrays.asList(availableTimesArray));

            // adapter
            arrayAdapter = new ArrayAdapter<String>(this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, availableTimesList);
            spinnerReservation.setAdapter(arrayAdapter);
            isReservationConfirmed = false;
            spinnerReservation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedTime = availableTimesList.get(position);

                    if (!isReservationConfirmed) {
                        // remove selected time from list
                        if (!selectedTime.equals("Please choose the time for your reservation")) {
                            tvReservationDuration.setText(selectedTime);
                            btnConfirmReservation.setVisibility(View.VISIBLE);
                          //  availableTimesList.remove(position);
                         //   arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        if (!selectedTime.equals("Please choose the time for your reservation")) {
                            availableTimesList.remove(position);
                            arrayAdapter.notifyDataSetChanged();
                        }
                     }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                tvReservation.setText("");
                }
            });

            btnConfirmReservation.setOnClickListener(v-> confirmReservation());

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etDob.setText(selectedDate);
            tvReservation.setText(selectedDate);
        }, year, month, day);

        long now = calendar.getTimeInMillis();
        datePickerDialog.getDatePicker().setMinDate(now);
        datePickerDialog.show();
    }

    private void confirmReservation() {
        Log.d("BikeID: ", "The Bike ID which is passed on is: " + bikeID);

        String userEmail = tvCustomerEmail.getText().toString();
        int bikeId = this.bikeID;
        String pickUpDate = tvReservation.getText().toString();
        String duration = tvReservationDuration.getText().toString();
        String status = "Confirmed";

       long result = dbHelper.addReservation(userEmail, bikeId, pickUpDate, duration, status);

        if (result != -1) {
            boolean bikeStatusUpdated = dbHelper.updateBikeStatus(bikeId, "Reserved");
            if (bikeStatusUpdated) {
                Toast.makeText(this, bike.getName() + " has been successfully reserved!", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Your Reservation ID is: " + result, Toast.LENGTH_SHORT).show();
                isReservationConfirmed = true;
                SharedPreferences sharedPreferences = getSharedPreferences("reservations", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("duration", duration);
                editor.apply();

                removeReservedTime(duration);
                startActivity(new Intent(ReservationActivity.this, NavigationBar.class));

            }
        }
    }

    private void removeReservedTime(String duration) {
        Log.d("Before Removal", "availableTimesList: " + availableTimesList.toString());
        Iterator<String> iterator = availableTimesList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(duration)) {
                iterator.remove();
                break;
            }
        }
        arrayAdapter.notifyDataSetChanged();
        Log.d("After Removal", "availableTimesList: " + availableTimesList.toString());
    }
}