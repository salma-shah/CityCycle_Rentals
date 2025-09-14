package com.example.citycycle_rentals;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.citycycle_rentals.databinding.ActivityNavigationBarBinding;


public class NavigationBar extends AppCompatActivity {
    ActivityNavigationBarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNavigationBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout, new HomeFragment())
                    .commit();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new HomeFragment())
                        .commit();
            } else if (itemId == R.id.bookings) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new RentalHistoryFragment())
                        .commit();
            } else if (itemId == R.id.profile) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new ProfileFragment())
                        .commit();
            }
            return true;
        });

    }
}
