package com.example.producttest.model;

import java.io.Serializable;

public class Product implements Serializable {
    public String id;
    String name;
    int price;
    String des;
    byte[] image;

    public Product() {
    }

    public Product(String name, int price, String des) {
        this.name = name;
        this.price = price;
        this.des = des;
    }

    public Product(String id, String name, int price, String des) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
