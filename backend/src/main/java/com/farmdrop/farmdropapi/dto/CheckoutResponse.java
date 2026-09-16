package com.farmdrop.farmdropapi.dto;

public class CheckoutResponse {
    private boolean canCheckout;
    private String message;

    private double productsTotal;
    private double boxesTotal;
    private double delivery;
    private double total;

    public CheckoutResponse(boolean canCheckout, String message,
                            double productsTotal, double boxesTotal,
                            double delivery, double total) {
        this.canCheckout = canCheckout;
        this.message = message;
        this.productsTotal = productsTotal;
        this.boxesTotal = boxesTotal;
        this.delivery = delivery;
        this.total = total;
    }

    public boolean isCanCheckout() { return canCheckout; }
    public String getMessage() { return message; }

    public double getProductsTotal() { return productsTotal; }
    public double getBoxesTotal() { return boxesTotal; }
    public double getDelivery() { return delivery; }
    public double getTotal() { return total; }
}
