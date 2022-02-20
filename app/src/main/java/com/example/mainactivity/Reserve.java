package com.example.mainactivity;

import java.io.Serializable;

public class Reserve implements Serializable {

    private String roomID;
    private String dateStart;
    private String dateEnd;
    private int cost;
    private String image0;
    private double rating;

    public Reserve(String roomID, String dateStart, String dateEnd, int cost, String image0, double rating) {
        this.roomID = roomID;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.cost = cost;
        this.image0 = image0;
        this.rating = rating;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getImage0() {
        return image0;
    }

    public void setImage0(String image0) {
        this.image0 = image0;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
