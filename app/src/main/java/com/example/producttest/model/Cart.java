package com.example.producttest.model;

public class Cart {
    int total;
    String time;

    public Cart(int total, String time) {
        this.total = total;
        this.time = time;
    }

    public Cart() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
