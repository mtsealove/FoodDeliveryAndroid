package com.mtsealove.github.food_delivery.Entity;


public class LastOrder {
    String OrderTime, ItemName, BusinessName;
    int Price;

    public LastOrder(String orderTime, String itemName, String businessName, int price) {
        OrderTime = orderTime;
        ItemName = itemName;
        BusinessName = businessName;
        Price = price;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
