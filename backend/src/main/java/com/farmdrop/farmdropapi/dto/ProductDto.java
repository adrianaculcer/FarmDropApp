package com.farmdrop.farmdropapi.dto;

public class ProductDto {
    private String id;      // ex: "tomatoes"
    private String name;
    private double price;
    private String unit;
    private String image;   // ex: "assets/images/tomatoes.jpg"

    public ProductDto(String id, String name, double price, String unit, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.image = image;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getUnit() { return unit; }
    public String getImage() { return image; }
}
