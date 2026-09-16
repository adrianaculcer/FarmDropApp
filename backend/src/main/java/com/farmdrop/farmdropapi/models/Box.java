package com.farmdrop.farmdropapi.models;


import com.farmdrop.farmdropapi.exceptions.BulkOrderException;

import java.util.HashMap;
import java.util.Map;

public class Box extends Product{


    private Map<Product, Double> items;  // products inside the box  |   quantity

    public Box(String name, int id) {
        super(name, 0, id, "box");
        this.items = new HashMap<>();
    }

    public void addProduct(Product product, double quantity)
    {
        items.put(product, quantity);
    }
    ///How does a box calculate the price?
    // Answer:
    // It iterates through all the products from the hash map and calls the
    // method calculatePrice for each. This way, polymorphism is used and respects
    // all the rules for each product: vegetable, fruit, corn (discount over 50)

    @Override
    public double calculatePrice(double quantity) throws BulkOrderException {
        double boxPrice = 0;
        for(Product p : items.keySet())
        {
            double productQuantity = items.get(p);
            boxPrice += p.calculatePrice(productQuantity); // Box nu stie tipul concret, de aceea apelam metoda
        }
        return boxPrice * quantity;
    }


    public Map<Product, Double> getItems() {
        return items;
    }
    public void setItems(Map<Product, Double> items) {
        this.items = items;
    }

}
