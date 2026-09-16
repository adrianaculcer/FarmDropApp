package com.farmdrop.farmdropapi.models;

import com.farmdrop.farmdropapi.exceptions.BulkOrderException;

public class SweetCorn extends Product{

    public SweetCorn(String name, double price, int id, String unit)
    {
        super(name, price, id, unit);
    }

    @Override
    public double calculatePrice(double quantity) throws BulkOrderException {
        if(quantity < 50)
            return price * quantity;
        else
            if(quantity < 100)
                return price * 0.85 * quantity;
            else
            {
                throw new BulkOrderException ("Contact us for an order over 100 sweetcorns");
            }
    }
}
