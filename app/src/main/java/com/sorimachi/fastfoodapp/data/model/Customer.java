package com.sorimachi.fastfoodapp.data.model;

import java.util.ArrayList;

public class Customer {
    private String phone, name, sex, address;
    private int startDate, birthday;
    private ArrayList<String> lstShop;

    public Customer(){}

    public Customer(String phone, String name, String sex, String address, int startDate, int birthday, ArrayList<String> lstShop) {
        this.phone = phone;
        this.name = name;
        this.sex = sex;
        this.address = address;
        this.startDate = startDate;
        this.birthday = birthday;
        this.lstShop = lstShop;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public ArrayList<String> getLstShop() {
        return lstShop;
    }

    public void setLstShop(ArrayList<String> lstShop) {
        this.lstShop = lstShop;
    }
}
