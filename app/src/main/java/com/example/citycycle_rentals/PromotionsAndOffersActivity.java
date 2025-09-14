package com.example.citycycle_rentals;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PromotionsAndOffersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_promotions_and_offers);

        // we hardcode memberships first
        List<Memberships> membership = new ArrayList<>();
        membership.add(new Memberships(8000, "/ day", "Rider's Basics", "Classic city bike with basic features", "Ideal for those who want to explore the city and sight see or students who have to commute for the day.", "Complimentary service: Free bike safety check before each rental.", "24/7 customer support for any rental inquiries.\n" ));
        membership.add(new Memberships(35000, "/ week", "Explorer’s Choice", "Electronic bike with added features", "Free entry and emergency repair at select local cycling workshops.", "Complimentary service: One free bike light with every rental.", "Dedicated customer support with immediate response times." ));
        membership.add(new Memberships(66000, "/ month", "Champion Cyclist", "Premium electric bike with a long battery life.","Perfect for businesses or individuals who need long-term bicycles for commuting or part of a wellness program.","Complimentary service: Free annual bike servicing and priority access to new models.\n","Personalized concierge service for exclusive member experiences.\n"));

        // hardcode promotions
        List<Promotion> promotions = new ArrayList<>();
        promotions.add(new Promotion(R.drawable.promo1, "Share your promo code with your friend and get your first ride free!","Click here for your random promo code"));
        promotions.add(new Promotion(R.drawable.promo2, "Bring your loved ones and enjoy a day of fun, games, and cycling together at our Race Course park!","Join the Ride"));
        promotions.add(new Promotion(R.drawable.promo3, "Unlock a surprise route and test your cycling skills! Complete the challenge to earn exclusive rewards.","Start the challenge"));
        promotions.add(new Promotion(R.drawable.promo4, "Experience the thrill of cycling under the stars with our guided night tours.\n","Register for the event"));

        // hardcode discounts
        List<Discount> discount = new ArrayList<>();
        discount.add(new Discount("Weekend Cycle", "25", "Enjoy 25% off on all rides this weekend! Limited-time deal for our loyal customers.", "30th of March, 2025"));
        discount.add(new Discount("Friends & Rides", "40", "Ride with friends! Book 3+ bikes together and get 40% off on your rental.", "21st of April, 2025"));
        discount.add(new Discount("Easy Commute with Us", "50", "Get unlimited bike rentals for a month at half the price! Best for daily commuters.", "28th of March, 2025"));
        discount.add(new Discount("Summer is Here", "35", "Planning a long ride for summer? Book a bike for 3+ days and enjoy 35% off!", "14th of June, 2025"));

        // setting up the recycler views in the activity
        setUpRV1(R.id.recyclerViewMemberships, membership);
        setUpRV2(R.id.recyclerViewPromotions, promotions);
        setUpRV3(R.id.recylerViewDiscounts, discount);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

private void setUpRV1(int recycleID, List<Memberships> membershipsList){
        RecyclerView recyclerView = findViewById(recycleID);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
        ));
        // populating recycler view with membership items
        MembershipsAdapter membershipsAdapter = new MembershipsAdapter(membershipsList);
        recyclerView.setAdapter(membershipsAdapter);
    }

    private void setUpRV2(int recycleID, List<Promotion> promotionList){
        RecyclerView recyclerView = findViewById(recycleID);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
        ));
        // populating recycler view with promotion items
        PromotionAdapter promotionAdapter = new PromotionAdapter(promotionList);
        recyclerView.setAdapter(promotionAdapter);
    }

    private void setUpRV3 (int recycleID, List<Discount> discountList) {
        RecyclerView recyclerView = findViewById(recycleID);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
        ));
        // populating recycler view with discount items
    DiscountAdapter discountAdapter = new DiscountAdapter(discountList);
    recyclerView.setAdapter(discountAdapter);
    }
}