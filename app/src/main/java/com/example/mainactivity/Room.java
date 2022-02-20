package com.example.mainactivity;

import java.io.Serializable;

public class Room implements Serializable {
    private int roomID;
    private String personNumber;
    private String roomType;
    private String image0;
    private String image1;
    private String image2;
    private String costPerDay;
    private String rating;

    private String isReserved;
    private String userID;

    private boolean love;



    public Room(int roomID, String personNumber, String roomType, String image0, String image1, String image2, String costPerDay, String rating, boolean love) {
        this.roomID = roomID;
        this.personNumber = personNumber;
        this.roomType = roomType;
        this.image0 = image0;
        this.image1 = image1;
        this.image2 = image2;
        this.costPerDay = costPerDay;
        this.rating = rating;
        this.love = love;
    }

    public Room(int roomID, String personNumber, String roomType, String image0, String image1, String image2, String costPerDay, String rating) {
        this.roomID = roomID;
        this.personNumber = personNumber;
        this.roomType = roomType;
        this.image0 = image0;
        this.image1 = image1;
        this.image2 = image2;
        this.costPerDay = costPerDay;
        this.rating = rating;
    }

    public Room(int roomID, String personNumber, String roomType, String image0, String image1, String image2, String costPerDay, String rating, String userID, String isReserved) {
        this.roomID = roomID;
        this.personNumber = personNumber;
        this.roomType = roomType;
        this.image0 = image0;
        this.image1 = image1;
        this.image2 = image2;
        this.costPerDay = costPerDay;
        this.rating = rating;
        this.userID = userID;
        this.isReserved = isReserved;
    }


    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getCostPerDay() {
        return costPerDay;
    }

    public void setCostPerDay(String costPerDay) {
        this.costPerDay = costPerDay;
    }

    public String getImage0() {
        return image0;
    }

    public void setImage0(String image0) {
        this.image0 = image0;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getIsReserved() {
        return isReserved;
    }

    public void setIsReserved(String isReserved) {
        this.isReserved = isReserved;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean getLove() {
        return love;
    }

    public void setLove(boolean love) {
        this.love = love;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", personNumber=" + personNumber +
                ", roomType='" + roomType + '\'' +
                ", costPerDay=" + costPerDay +
                '}';
    }
}
