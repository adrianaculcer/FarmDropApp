package com.farmdrop.farmdropapi.dto;

import java.util.List;

public class CheckoutRequest {

    public static class CartItem {
        private String type; // "product" / "box"
        private String id;   // "tomatoes" / "box-salad-m"
        private int qty;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public int getQty() { return qty; }
        public void setQty(int qty) { this.qty = qty; }
    }

    private List<CartItem> items;

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
