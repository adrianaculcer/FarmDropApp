package com.farmdrop.farmdropapi.models;

import com.farmdrop.farmdropapi.exceptions.BulkOrderException;

public abstract class Product {
    // protected is visible to:
    // - same class
    // - same package
    // - same subclasses

    protected String name;
    protected double price;
    protected int id;
    protected String unit;
    public Product(String name, double price, int id, String unit) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.unit = unit;
    }

    // key for polymorphism
    public abstract double calculatePrice(double quantity) throws BulkOrderException;

    //Getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }



    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }



    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
