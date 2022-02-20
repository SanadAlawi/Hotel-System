package com.example.mainactivity;

public class Order {

    private int orderId;
    private int roomId;
    private String orderType;
    private String orderDescription;

    public Order(int orderId, int roomId, String orderType, String orderDescription) {
        this.orderId = orderId;
        this.roomId = roomId;
        this.orderType = orderType;
        this.orderDescription = orderDescription;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", roomId=" + roomId +
                ", orderType='" + orderType + '\'' +
                ", orderDescription='" + orderDescription + '\'' +
                '}';
    }
}
