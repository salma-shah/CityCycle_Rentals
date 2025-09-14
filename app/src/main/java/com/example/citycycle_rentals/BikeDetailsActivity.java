package com.example.citycycle_rentals;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class BikeDetailsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private TextView tvBikeName, tvBikeLocation, tvBikeCategory, tvPriceHour, tvPriceDay, tvPriceWeek,tvBikeDescription;
    private RatingBar rbBikeRating;
    private ImageView imgBikeImage;
    private Button btnRent, btnReserve;
    private int bikeID;
    private Bike bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bike_details);

        // initialize
        tvBikeName = findViewById(R.id.tvBikeName);
        tvBikeLocation = findViewById(R.id.tvBikeLocation);
        tvBikeCategory = findViewById(R.id.tvBikeCategory);
        tvPriceHour = findViewById(R.id.tvPriceHour);
        tvPriceDay = findViewById(R.id.tvPriceDay);
        tvPriceWeek = findViewById(R.id.tvPriceWeek);
        tvBikeDescription = findViewById(R.id.tvBikeDescription);
        rbBikeRating = findViewById(R.id.rbBikeRating);
        imgBikeImage = findViewById(R.id.imgBikeImage);
        btnReserve = findViewById(R.id.btnReserve);
        btnRent = findViewById(R.id.btnRent);

        dbHelper = new DBHelper(this);

        //we get the bike id that was passed from the previous fragment to here with intent
        bikeID = getIntent().getIntExtra("bike_id", -1); // -1 is default value, if doesn't exist
        Log.d("BikeDetailsActivity", "Bike ID received is: " + bikeID);

        // if bike id is valid, then get bike details of that id from table
        if (bikeID != -1) {
             bike = dbHelper.getBikeDetails(bikeID);

            if (bike != null) {

                tvBikeName.setText(bike.getName());
                tvBikeLocation.setText(bike.getLocation());
                tvBikeCategory.setText(bike.getCategory());
                tvPriceHour.setText("LKR " + bike.getPriceHour());
                tvPriceDay.setText("LKR " + bike.getPriceDay());
                tvPriceWeek.setText("LKR " + bike.getPriceWeek());
                tvBikeDescription.setText(bike.getDescription());
                rbBikeRating.setRating(bike.getRating());

                if (bike.getBikeImage() != 0){
                    int image = bike.getBikeImage();
                    imgBikeImage.setImageResource(image);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bike.getBikeImage(), 0, bike.getBikeImage().length);
//            holder.imgBikeImage.setImageBitmap(bitmap);
                }
                else {
                    imgBikeImage.setImageResource(R.drawable.default_bike);
                }

                String bikeStatus = bike.getStatus();
                if (bikeStatus.equals("Rented")) {
                    btnRent.setVisibility(View.GONE);  // removing rent button since that bike is already currently rented
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btnReserve.getLayoutParams();
                    params.weight = 2;
                    btnReserve.setLayoutParams(params); // let reserve button take up most space, since rent button will not be visible
                }
                else {
                    btnRent.setVisibility(View.VISIBLE);
                }

            } else {
                tvBikeName.setText("No Bike Name");
                tvBikeLocation.setText("No Location");
                tvBikeCategory.setText("No Category");
                tvPriceHour.setText("LKR 0");
                tvPriceDay.setText("LKR 0");
                tvPriceWeek.setText("LKR 0");
                tvBikeDescription.setText("No Description");
                rbBikeRating.setRating(0);
                imgBikeImage.setImageResource(R.drawable.baseline_person_24);
            }
        } else {
            Toast.makeText(this, "Invalid Bike ID", Toast.LENGTH_SHORT).show();
        }

        btnRent.setOnClickListener(v-> {
            // navigating to rent page activity, passing bike id
                Intent intent = new Intent(BikeDetailsActivity.this, RentalActivity.class);
                intent.putExtra("bike_id", bike.getBikeId());
                startActivity(intent);
        });

        btnReserve.setOnClickListener(v-> {
            // navigating to reserve page activity, passing bike id
            Intent intent = new Intent(BikeDetailsActivity.this, ReservationActivity.class);
            intent.putExtra("bike_id", bike.getBikeId());
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    }
