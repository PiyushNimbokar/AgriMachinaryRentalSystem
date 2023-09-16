package com.example.minor;

public class MyItem {
    private String Name;
    private String Deadline;
    private String ImageUrl;

    public MyItem() {
        // Empty constructor needed for Firebase
    }

    public MyItem(String name, String deadline, String imageUrl) {
        this.Name = name;
        this.Deadline = deadline;
        this.ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public String getDeadline() {
        return Deadline;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}
