package com.example;

public class Bazaar {
    private int itemId;
    private int userId;
    private String itemName;
    private int count;
    private int price;
    private String itemType;

    public Bazaar(int itemId, int userId, String itemName, int count, int price, String itemType) {
        this.itemId = itemId;
        this.userId = userId;
        this.itemName = itemName;
        this.count = count;
        this.price = price;
        this.itemType = itemType;
    }

    // Getters and setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
}