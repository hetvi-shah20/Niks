package com.example.niks.Model;

public class Cart {

    String cart_id,product_id,product_name,product_description,product_image,product_qty,product_unit_price,product_amount,product_weigth;

    public Cart(String cart_id, String product_id, String product_name, String product_description, String product_image, String product_qty, String product_unit_price, String product_amount, String product_weigth) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_image = product_image;
        this.product_qty = product_qty;
        this.product_unit_price = product_unit_price;
        this.product_amount = product_amount;
        this.product_weigth = product_weigth;
    }

    public Cart() {
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getProduct_unit_price() {
        return product_unit_price;
    }

    public void setProduct_unit_price(String product_unit_price) {
        this.product_unit_price = product_unit_price;
    }

    public String getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(String product_amount) {
        this.product_amount = product_amount;
    }

    public String getProduct_weigth() {
        return product_weigth;
    }

    public void setProduct_weigth(String product_weigth) {
        this.product_weigth = product_weigth;
    }
}
