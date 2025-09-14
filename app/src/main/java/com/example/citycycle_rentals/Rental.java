package com.example.citycycle_rentals;

public class Rental {
    private int rentalId;
    private String userEmail;
    private int bikeId;
    private String startDate;
    private String endDate;
    private String duration;
    private String status;
    private Bike bike;

    public Rental(int rentalId, String userEmail, int bikeId, String startDate, String endDate, String duration, String status, Bike bike) {
        this.rentalId = rentalId;
        this.userEmail = userEmail;
        this.bikeId = bikeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.status = status;
        this.bike = bike;
    }

    public Rental(int rentalId, String userEmail, int bikeId, String startDate, String endDate, String duration, String status) {
        this.rentalId = rentalId;
        this.userEmail = userEmail;
        this.bikeId = bikeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.status = status;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }
}