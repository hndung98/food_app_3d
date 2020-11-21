package com.sorimachi.fastfoodapp.data.model;

public class Food {
    private int priority;
    private String name, unit, image;
    private int price;

    public Food(){}

    public Food(int priority, String name, int price, String unit, String image) {
        this.priority = priority;
        this.name = name;
        this.unit = unit;
        this.image = image;
        this.price = price;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int foodCode) {
        this.priority = foodCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
