package com.example.citycycle_rentals;

public class Promotion {
    private String description, promoBtn;
    private int promoImage;

    public Promotion(int promoImage, String description, String promoBtn) {
        this.promoImage = promoImage;
        this.description = description;
        this.promoBtn = promoBtn;
    }
    public int getPromoImage() {
        return promoImage;
    }
    public String getDescription() {
        return description;
    }
    public String getPromoBtn() {
        return promoBtn;
    }

}
