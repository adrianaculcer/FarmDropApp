package com.farmdrop.farmdropapi.models;
import com.farmdrop.farmdropapi.exceptions.BulkOrderException;
import com.farmdrop.farmdropapi.exceptions.Under100LeiException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// An order represents a customer buying one or more boxes or products
// 1. Building - adding items
// 2. Validation
// 3. Place the order

public class Order {
    private int id;
    private User user;
    private Map<Product, Integer> items;        //   product  | quantity
    private double totalPrice;
    private LocalDateTime orderDate;
    private static int idIndex = 1;

    public Order(User user)
    {
        this.id = idIndex ++;
        this.user = user;
        this.items = new HashMap<>();
        this.orderDate = LocalDateTime.now();
        this.totalPrice = 0;
    }

    public void addProduct(Product p, int quantity)
    {
            items.put(p, quantity);
    }

    public void calculateTotal() throws BulkOrderException {
        totalPrice = 0;
        for(Product p : items.keySet())
        {
            int quantity = items.get(p);
            totalPrice += p.calculatePrice(quantity);

        }
    }

    public double getTotalPrice()
    {
        return this.totalPrice;
    }

    public boolean containsOnlyIndividualProducts()
    {
        for(Product p :  items.keySet())
        {
            if(p instanceof Box)
                return false;
        }
        return true;
    }

    public boolean canPlaceOrder() throws BulkOrderException {
        calculateTotal();
        // if the basket contains just individual products under the cost of 100 lei
        // there is no way the order can be placed.
        if(containsOnlyIndividualProducts() && totalPrice < 100)
            return false;

        return true;
    }

    public void placeOrder() throws BulkOrderException {

        if(!canPlaceOrder())
        {
            throw new Under100LeiException("Order cannot be placed. Minimum order is 100 lei for individual products.");
        }
        System.out.println("Order #" + id + " placed successfully!");
        System.out.println("Total price: " + totalPrice);
    }

}


