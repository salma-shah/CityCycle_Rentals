package com.example.citycycle_rentals;

public class Discount {
    private String percentage, discountName;
    private String discountInfo, validity;

    public Discount(String discountName, String percentage, String discountInfo, String validity) {
        this.discountName = discountName;
        this.percentage = percentage;
        this.discountInfo = discountInfo;
        this.validity = validity;
    }

    public String getDiscountName() {
        return discountName;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getDiscountInfo() {
        return discountInfo;
    }

    public String getValidity() {
        return validity;
    }
}
