package com.mtsealove.github.food_delivery.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuList implements Serializable {
    ArrayList<Menu> list;

    public MenuList() {
        list = new ArrayList<>();
    }

    public ArrayList<Menu> getList() {
        return list;
    }

    public void setList(ArrayList<Menu> list) {
        this.list = list;
    }
}
