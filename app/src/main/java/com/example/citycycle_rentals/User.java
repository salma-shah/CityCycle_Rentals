package com.example.citycycle_rentals;

public class User {
    private int id;
    private String firstName, lastName, contact, dob, email, password;
    private byte[] profileImage;
    private String cardType, cardNumber, cardHolderName, expiryDate, ccv;

    public User(int id, String firstName, String lastName, String contact, String dob, String email, String password, byte[] profileImage,  String cardType, String cardNumber, String cardHolderName, String expiryDate, String ccv) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.ccv = ccv;
        this.cardType = cardType;
    }

    public User(int id, String firstName, String lastName, String contact, String dob, String password, String email, byte[] profileImage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
    }

    public User(String firstName, String lastName, String contact, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.email = email;
    }

    public User(String email, String cardType, String cardNumber, String cardHolderName, String expiryDate, String ccv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.ccv = ccv;
        this.cardType = cardType;
        this.email = email;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCcv() {
        return ccv;
    }

    public String getCardType() {
        return cardType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContact() {
        return contact;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public byte[] getProfileImage() {
        return profileImage;
    }

}
