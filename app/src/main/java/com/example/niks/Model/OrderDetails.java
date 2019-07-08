package com.example.niks.Model;

public class OrderDetails {

    String ProductName,Price,Quantity,Amount;

    public OrderDetails() {
    }

    public OrderDetails(String productName, String price, String quantity, String amount) {
        ProductName = productName;
        Price = price;
        Quantity = quantity;
        Amount = amount;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
