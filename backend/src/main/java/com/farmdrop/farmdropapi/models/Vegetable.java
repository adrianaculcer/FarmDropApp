package com.farmdrop.farmdropapi.models;

import com.farmdrop.farmdropapi.exceptions.BulkOrderException;

public class Vegetable extends Product{
    private String harvestSeason; // summer, autumn, winter, spring

    public Vegetable(String name, double price, int id, String unit, String harvestSeason) {
        super(name, price, id, unit);
        this.harvestSeason = harvestSeason;
    }

    @Override
    public double calculatePrice(double quantity) throws BulkOrderException {
        return price * quantity;
    }

    //Getter and setter
    public String getHarvestSeason() {
        return harvestSeason;
    }
    public void setHarvestSeason(String harvestSeason) {
        this.harvestSeason = harvestSeason;
    }
}
