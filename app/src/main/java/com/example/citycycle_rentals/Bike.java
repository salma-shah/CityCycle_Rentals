package com.example.citycycle_rentals;

public class Bike {
    private int bikeId;
    private String name, category, description,location;
    private double priceHour,priceWeek,priceDay;
    private float rating;
    private String status;
    private int bikeImage;

    public Bike(int bikeId, String name, String category, String description, String location, double priceHour, double priceDay, double priceWeek, String status, float rating, int bikeImage) {
        this.bikeId = bikeId;
        this.name = name;
        this.category = category;
        this.description = description;
        this.location = location;
        this.priceHour = priceHour;
        this.priceWeek = priceWeek;
        this.priceDay = priceDay;
        this.status = status;
        this.rating = rating;
        this.bikeImage = bikeImage;
    }

    public Bike(int bikeId, String name, String location, int bikeImage) {
        this.bikeId = bikeId;
        this.name = name;
        this.location = location;
        this.bikeImage = bikeImage;
    }

    public Bike(String name, String location, String category, int bikeImage, String status) {
        this.name = name;
        this.location = location;
        this.category = category;
        this.bikeImage = bikeImage;
        this.status = status;
    }

    public int getBikeId() {
        return bikeId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public double getPriceHour() {
        return priceHour;
    }

    public double getPriceWeek() {
        return priceWeek;
    }

    public double getPriceDay() {
        return priceDay;
    }

    public String getStatus() {
        return status;
    }

    public float getRating() {
        return rating;
    }

    public int getBikeImage() {
        return bikeImage;
    }

   // public void setStatus(String status) {
     //   this.status = status;
  //  }
}
