package com.example.inventario;

import android.os.Parcel;
import android.os.Parcelable;

public class Product {
    private String nit;
    private String name;
    private String description;
    private double price;
    private int quantity;

    public Product() {
    }

    public Product(String nit, String name, String description, double price, int quantity) {
        this.nit = nit;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
