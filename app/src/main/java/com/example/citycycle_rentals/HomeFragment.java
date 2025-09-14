package com.example.citycycle_rentals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewBikes;
    private BikeAdapter adapter;
    private List<Bike> bikeList;
    private SearchView searchViewLocation;
    private Spinner spinnerFilterCategory, spinnerSortPrice;
    private TextView tvWelcomeUser;
    private ImageView imgPromoOffer;
    private DBHelper dbHelper;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DBHelper(requireActivity());
        tvWelcomeUser = view.findViewById(R.id.tvWelcomeUser);
        imgPromoOffer = view.findViewById(R.id.imgPromoOffer);

        // getting user name
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");
        if (userEmail != null) {
            String fullName = dbHelper.getUserName(userEmail);
            tvWelcomeUser.setText("Hello, " + fullName);
        } else {
            tvWelcomeUser.setText("Hello, User!"); // default
        }
        // takes user to promotions page
        imgPromoOffer.setOnClickListener(v-> {
            Intent intent = new Intent(requireActivity(), PromotionsAndOffersActivity.class);
            startActivity(intent);
        });

        // populating the RV with bikes and initializing
        bikeList = dbHelper.getAllBikes();
        recyclerViewBikes = view.findViewById(R.id.recyclerViewBikes);
        searchViewLocation = view.findViewById(R.id.searchViewLocation);
        spinnerFilterCategory = view.findViewById(R.id.spinnerFilterCategory);
        spinnerSortPrice = view.findViewById(R.id.spinnerSortPrice);

        if (bikeList == null) {
            // array list
            bikeList = new ArrayList<>();
        }
        // using the bike adapter
        adapter = new BikeAdapter(bikeList);

        recyclerViewBikes.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerViewBikes.setAdapter(adapter);

        setUpSearch();
        setUpFilters();

        return view;
    }

    private void setUpSearch() {
        searchViewLocation.setQueryHint("Search bikes by location");
        searchViewLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterBikes();
                return true;
            }
        });
    }

    private void setUpFilters() {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterBikes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinnerFilterCategory.setOnItemSelectedListener(listener);
        spinnerSortPrice.setOnItemSelectedListener(listener);
    }
    private void filterBikes() {
        String searchedText = searchViewLocation.getQuery().toString().toLowerCase();
        String selectedCategory = spinnerFilterCategory.getSelectedItem().toString();
        String sortedPrice = spinnerSortPrice.getSelectedItem().toString().trim();
        // logging
        Log.d("BikeFilter", "Selected category: " + selectedCategory);
        Log.d("BikeFilter", "Search query: " + searchedText);
        Log.d("BikeFilter", "Sorted Price Range: " + sortedPrice);

        List <Bike> filteredList = new ArrayList<>();

        for (Bike bike : bikeList) {
            // checking if search value is empty, or if it matches a location, even partially
            boolean searchMatch= searchedText.isEmpty() || bike.getLocation().toLowerCase().contains(searchedText);
            // checking if category choice value is all bikes, or if it is a category value
            boolean categoryMatch= selectedCategory.equals("All Bikes") || bike.getCategory().trim().equalsIgnoreCase(selectedCategory);

            if (categoryMatch && searchMatch) {
                filteredList.add(bike);
            }
        }

        // sorting by price ranges; ascending or descending
        if (sortedPrice.equals("Sort by price: Low - High")) {
            filteredList.sort((p1, p2) -> Double.compare(p1.getPriceHour(), p2.getPriceHour()));
        } else if (sortedPrice.equals("Sort by price: High - Low")) {
            filteredList.sort((p1, p2) -> Double.compare(p2.getPriceHour(), p1.getPriceHour()));
        }

        // update the adapter and notify it
        adapter.updateList(filteredList);
        adapter.notifyDataSetChanged();
    }

}
