package com.farmdrop.farmdropapi.models;


import com.farmdrop.farmdropapi.exceptions.BulkOrderException;

public class Fruit extends Product {

    public Fruit (String name, double price, int id, String unit)
    {
        super(name, price, id, unit);
    }

    @Override
    public double calculatePrice(double quantity) throws BulkOrderException
    {
        return price * quantity;
    }

}
