package com.sorimachi.fastfoodapp.data.model;

public class Order {
    private int foodCode, price, amount, discount;

    public Order(){}

    public Order(int foodCode, int price, int amount, int discount) {
        this.foodCode = foodCode;
        this.price = price;
        this.amount = amount;
        this.discount = discount;
    }

    public int getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(int foodCode) {
        this.foodCode = foodCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
