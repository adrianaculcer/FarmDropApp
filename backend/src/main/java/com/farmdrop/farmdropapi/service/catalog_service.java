package com.farmdrop.farmdropapi.service;

import com.farmdrop.farmdropapi.dto.BoxDto;
import com.farmdrop.farmdropapi.dto.BoxItemDto;
import com.farmdrop.farmdropapi.dto.ProductDto;
import com.farmdrop.farmdropapi.exceptions.BulkOrderException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class catalog_service {

    //  tin catalogul in memorie ca sa pot sa:
    // 1) dau lista catre frontend (products / boxes)
    // 2) calculez totaluri la checkout dupa id (fara hardcodare in controller)
    private final Map<String, ProductDto> productsById = new LinkedHashMap<>();
    private final Map<String, BoxDto> boxesById = new LinkedHashMap<>();

    public catalog_service() {
        // products
        ProductDto tomatoes = new ProductDto("tomatoes", "Roze Tomatoes", 10.00, "kg", "assets/images/tomatoes.jpg");
        ProductDto carrots = new ProductDto("carrots", "Carrots", 4.00, "kg", "assets/images/carrots.jpg");
        ProductDto sweetcorn = new ProductDto("sweetcorn", "Sweet Corn", 3.50, "buc", "assets/images/sweetcorn.jpg");

        productsById.put(tomatoes.getId(), tomatoes);
        productsById.put(carrots.getId(), carrots);
        productsById.put(sweetcorn.getId(), sweetcorn);

        // boxes (pret fix 50, exact ca in boxes.html)
        BoxDto soup = new BoxDto(
                "box-soup-m",
                "Soup Box",
                50,
                "assets/images/box-soup.jpg",
                "Classic soup base veggies · 3–4 people",
                List.of(
                        new BoxItemDto("Carrots", 2, "kg"),
                        new BoxItemDto("Parsnip", 0.5, "kg"),
                        new BoxItemDto("Parsley", 0.5, "kg"),
                        new BoxItemDto("White onion", 1, "kg"),
                        new BoxItemDto("Garlic", 0.3, "kg"),
                        new BoxItemDto("Kapia", 1, "kg")
                )
        );

        BoxDto salad = new BoxDto(
                "box-salad-m",
                "Salad Box",
                50,
                "assets/images/box-salad.jpg",
                "Fresh salads · 3–4 people",
                List.of(
                        new BoxItemDto("Pink tomatoes", 2, "kg"),
                        new BoxItemDto("Kapia pepper", 1, "kg"),
                        new BoxItemDto("Red onion", 0.5, "kg"),
                        new BoxItemDto("Salad", 2, "buc"),
                        new BoxItemDto("Sweetcorn", 3, "buc")
                )
        );

        boxesById.put(soup.getId(), soup);
        boxesById.put(salad.getId(), salad);
    }

    public List<ProductDto> get_products_dto() {
        return new ArrayList<>(productsById.values());
    }

    public List<BoxDto> get_boxes_dto() {
        return new ArrayList<>(boxesById.values());
    }

    // productQty: id -> qty (ex: tomatoes -> 2)
    public double calc_products_total(Map<String, Double> productQty) throws BulkOrderException {
        if (productQty == null || productQty.isEmpty()) return 0;

        double total = 0;
        for (Map.Entry<String, Double> e : productQty.entrySet()) {
            ProductDto p = productsById.get(e.getKey());
            if (p == null) continue;

            double qty = e.getValue() == null ? 0 : e.getValue();
            total += p.getPrice() * qty;
        }
        return total;
    }

    // boxQty: id -> qty (ex: box-soup-m -> 1)
    public double calc_boxes_total(Map<String, Double> boxQty) {
        if (boxQty == null || boxQty.isEmpty()) return 0;

        double total = 0;
        for (Map.Entry<String, Double> e : boxQty.entrySet()) {
            BoxDto b = boxesById.get(e.getKey());
            if (b == null) continue;

            double qty = e.getValue() == null ? 0 : e.getValue();
            total += b.getPrice() * qty; // pret fix
        }
        return total;
    }
}
