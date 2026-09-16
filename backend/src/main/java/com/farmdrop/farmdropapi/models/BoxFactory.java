package com.farmdrop.farmdropapi.models;

public class BoxFactory {

    public static Box soupBox()
    {
        Box soupBox = new Box("Soup Box", 1);

        Vegetable carrots = new Vegetable("Carrots", 3.0, 101, "kg", "summer");
        Vegetable parsley = new Vegetable("Parsley", 18.0, 102, "kg", "summer");
        Vegetable parsnip = new Vegetable("Parsnip", 18.0, 103, "kg", "summer");
        Vegetable onion = new Vegetable("White Onion", 5.0, 104, "kg", "summer");

        soupBox.addProduct(carrots, 1);
        soupBox.addProduct(parsley, 0.5);
        soupBox.addProduct(parsnip, 0.5);
        soupBox.addProduct(onion, 1);

        return soupBox;
    }

}
