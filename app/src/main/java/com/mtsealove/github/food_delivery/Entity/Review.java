package com.mtsealove.github.food_delivery.Entity;

public class Review {
    String UserName, Content, Time;

    public Review(String userName, String content, String time) {
        UserName = userName;
        Content = content;
        Time = time;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    @Override
    public String toString() {
        return "Review{" +
                "UserName='" + UserName + '\'' +
                ", Content='" + Content + '\'' +
                ", Time='" + Time + '\'' +
                '}';
    }
}
