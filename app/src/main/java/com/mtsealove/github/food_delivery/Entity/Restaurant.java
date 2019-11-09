package com.mtsealove.github.food_delivery.Entity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

//식당 객체
public class Restaurant implements Serializable {
    String ID;
    String BusinessName;
    String BusinessAddress;
    int Category;
    String ProfileImage;
    double distance;

    public Restaurant(String ID, String businessName, String businessAddress, int category, String profileImage) {
        this.ID = ID;
        BusinessName = businessName;
        BusinessAddress = businessAddress;
        Category = category;
        ProfileImage = profileImage;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getBusinessAddress() {
        return BusinessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        BusinessAddress = businessAddress;
    }

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    //사용자와의 거리 계산
    public void setDistance(Context context, Location UserLocation) {
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> list = geocoder.getFromLocationName(BusinessAddress, 10);
            if (list != null) {
                double latitude = list.get(0).getLatitude();
                double longitude = list.get(0).getLongitude();

                distance=calcDistance(latitude, longitude, UserLocation.getLatitude(), UserLocation.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //거리 계산
    private double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1609.344;

        return (dist);
    }


    // This function converts decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "ID='" + ID + '\'' +
                ", BusinessName='" + BusinessName + '\'' +
                ", BusinessAddress='" + BusinessAddress + '\'' +
                ", Category=" + Category +
                ", ProfileImage='" + ProfileImage + '\'' +
                '}';
    }
}
