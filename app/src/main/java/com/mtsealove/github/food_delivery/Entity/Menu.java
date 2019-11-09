package com.mtsealove.github.food_delivery.Entity;

public class Menu {
    int ID, Price;
    String ItemName, ManagerID, ImagePath, Des;

    public Menu(int ID, int price, String itemName, String managerID, String imagePath, String des) {
        this.ID = ID;
        Price = price;
        ItemName = itemName;
        ManagerID = managerID;
        ImagePath = imagePath;
        Des = des;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getManagerID() {
        return ManagerID;
    }

    public void setManagerID(String managerID) {
        ManagerID = managerID;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "ID=" + ID +
                ", Price=" + Price +
                ", ItemName='" + ItemName + '\'' +
                ", ManagerID='" + ManagerID + '\'' +
                ", ImagePath='" + ImagePath + '\'' +
                ", Des='" + Des + '\'' +
                '}';
    }
}
