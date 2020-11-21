package com.sorimachi.fastfoodapp.data.model;

import java.util.ArrayList;

public class Bill {
    private int billCode, customerCode, shopCode, ymd, hms, status;
    private ArrayList<Order> OrdersList;

    public Bill(){}

    public Bill(int billCode, int customerCode, int shopCode, int ymd, int hms, int status, ArrayList<Order> ordersList) {
        this.billCode = billCode;
        this.customerCode = customerCode;
        this.shopCode = shopCode;
        this.ymd = ymd;
        this.hms = hms;
        this.status = status;
        OrdersList = ordersList;
    }

    public int getBillCode() {
        return billCode;
    }

    public void setBillCode(int billCode) {
        this.billCode = billCode;
    }

    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }

    public int getShopCode() {
        return shopCode;
    }

    public void setShopCode(int shopCode) {
        this.shopCode = shopCode;
    }

    public int getYmd() {
        return ymd;
    }

    public void setYmd(int ymd) {
        this.ymd = ymd;
    }

    public int getHms() {
        return hms;
    }

    public void setHms(int hms) {
        this.hms = hms;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Order> getOrdersList() {
        return OrdersList;
    }

    public void setOrdersList(ArrayList<Order> ordersList) {
        OrdersList = ordersList;
    }
}
