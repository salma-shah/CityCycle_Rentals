package com.example.citycycle_rentals;

public class Memberships {
    private double price;
    private String duration, type, detail1, detail2, detail3, detail4;

    public Memberships(double price, String duration, String type, String detail1, String detail2, String detail3, String detail4) {
        this.price = price;
        this.duration = duration;
        this.type = type;
        this.detail1 = detail1;
        this.detail2 = detail2;
        this.detail3 = detail3;
        this.detail4 = detail4;
    }

    public double getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public String getDetail1() {
        return detail1;
    }

    public String getDetail2() {
        return detail2;
    }

    public String getDetail3() {
        return detail3;
    }

    public String getDetail4() {
        return detail4;
    }
}