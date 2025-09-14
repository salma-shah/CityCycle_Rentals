package com.example.citycycle_rentals;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RentalHistoryFragment extends Fragment {
    private DBHelper dbHelper;
    private RecyclerView recyclerViewRentalHistory;
    private RentalAdapter rentalAdapter;
    private List<Rental> rentalList;

    public RentalHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental_history, container, false);

        // getting rentals of specific user
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // setting it in order from most recent rentals to older ones
        recyclerViewRentalHistory = view.findViewById(R.id.recyclerViewRentalHistory);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(requireContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewRentalHistory.setLayoutManager(linearLayoutManager);

        dbHelper = new DBHelper(requireContext());
        // getting all the rentals in the recycler view
        rentalList = dbHelper.getAllRentals(userEmail);
        rentalAdapter = new RentalAdapter(getContext(), rentalList);
        recyclerViewRentalHistory.setAdapter(rentalAdapter);
        return view;
        }
    }
