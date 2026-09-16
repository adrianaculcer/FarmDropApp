package com.farmdrop.farmdropapi.dto;

import java.util.List;

public class BoxDto {
    private String id;      // ex: "box-salad-m"
    private String name;
    private double price;   // fixed price for 1 box
    private String image;
    private String subtitle; // optional, ca sa pastrez exact textul tau
    private List<BoxItemDto> items;

    public BoxDto(String id, String name, double price, String image, String subtitle, List<BoxItemDto> items) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.subtitle = subtitle;
        this.items = items;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public String getSubtitle() { return subtitle; }
    public List<BoxItemDto> getItems() { return items; }
}
