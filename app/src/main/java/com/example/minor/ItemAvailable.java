package com.example.minor;

public class ItemAvailable {

    private String Name;
    private String UserId;
    private String ItemId;
    private String City;
    private String ModelYear;
    private String Deadline;
    private String RentCost;
    private String ImageUrl;
    private boolean Availability;

    public ItemAvailable() {
    }

    public ItemAvailable(String name, String modelYear, String deadline, String rentCost, String imageUrl, boolean availability, String city, String userId, String itemId) {
        this.Name = name;
        this.ModelYear = modelYear;
        this.Deadline = deadline;
        this.RentCost = rentCost;
        this.ImageUrl = imageUrl;
        this.Availability = availability;
        this.City = city;
        this.UserId = userId;
        this.ItemId = itemId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String key) {
        this.ItemId = key;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getModelYear() {
        return ModelYear;
    }

    public void setModelYear(String modelYear) {
        ModelYear = modelYear;
    }

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }

    public String getRentCost() {
        return RentCost;
    }

    public void setRentCost(String rentCost) {
        RentCost = rentCost;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public boolean getAvailability() {
        return Availability;
    }

    public void setAvailability(boolean availability) {
        Availability = availability;
    }
}





