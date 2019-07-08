package com.example.niks.Model;

public class Order {

    String OrderID,TotalAmount,Date;

    public Order() {
    }

    public Order(String orderID, String totalAmount, String date) {
        OrderID = orderID;
        TotalAmount = totalAmount;
        Date = date;
    }


    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
