package com.example.minor;

public class RentDetails {
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setRentalDeadline(String rentalDeadline) {
        this.rentalDeadline = rentalDeadline;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    private String userId;
    private String itemId;
    private String rentalDeadline;
    private String address;
    private String pincode;
    private String city;
    private String selectedDate;

    public RentDetails() {
        // Default constructor required for Firebase database
    }

    public RentDetails(String userId, String itemId, String rentalDeadline, String address, String pincode, String city, String selectedDate) {
        this.userId = userId;
        this.itemId = itemId;
        this.rentalDeadline = rentalDeadline;
        this.address = address;
        this.pincode = pincode;
        this.city = city;
        this.selectedDate = selectedDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getRentalDeadline() {
        return rentalDeadline;
    }

    public String getAddress() {
        return address;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCity() {
        return city;
    }

    public String getSelectedDate() {
        return selectedDate;
    }
}

