package com.mtsealove.github.food_delivery.Entity;


/*
  'OrderTime' => $row["OrderTime"],
        'Location' => $row["Location"],
        'currentLocation' => $row["currentLocation"],
        'ItemName' => $row["ItemName"],
        'Price' => $row["Price"],
        'ImagePath' => $row["ImagePath"],
        'StatusName' => $row["StatusName"]
 */
public class Order {
    String OrderTime, currentLocation, ItemName, ImagePath, StatusName;
    int Price;

    public Order(String orderTime, String currentLocation, String itemName, String imagePath, String statusName, int price) {
        OrderTime = orderTime;
        this.currentLocation = currentLocation;
        ItemName = itemName;
        ImagePath = imagePath;
        StatusName = statusName;
        Price = price;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }


}
