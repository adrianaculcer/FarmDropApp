package com.farmdrop.farmdropapi.controller;

import com.farmdrop.farmdropapi.dto.CheckoutRequest;
import com.farmdrop.farmdropapi.dto.CheckoutResponse;
import com.farmdrop.farmdropapi.exceptions.BulkOrderException;
import com.farmdrop.farmdropapi.service.catalog_service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class checkout_controller {

    private static final double MIN_SOLO_PRODUCTS = 100.0;
    private static final double DELIVERY_FEE = 5.0;

    private final catalog_service catalog;

    public checkout_controller(catalog_service catalog) {
        this.catalog = catalog;
    }

    @PostMapping("/api/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody(required = false) CheckoutRequest req) {
        try {
            Map<String, Double> productQty = new HashMap<>();
            Map<String, Double> boxQty = new HashMap<>();

            if (req != null && req.getItems() != null) {
                for (CheckoutRequest.CartItem it : req.getItems()) {
                    if (it == null || it.getId() == null) continue;

                    int qty = Math.max(0, it.getQty());
                    if (qty == 0) continue;

                    if ("product".equalsIgnoreCase(it.getType())) {
                        productQty.put(it.getId(), productQty.getOrDefault(it.getId(), 0.0) + qty);
                    } else if ("box".equalsIgnoreCase(it.getType())) {
                        boxQty.put(it.getId(), boxQty.getOrDefault(it.getId(), 0.0) + qty);
                    }
                }
            }

            boolean hasProducts = !productQty.isEmpty();
            boolean hasBoxes = !boxQty.isEmpty();

            if (!hasProducts && !hasBoxes) {
                return ResponseEntity.ok(new CheckoutResponse(false, "Cart is empty.", 0, 0, 0, 0));
            }

            double productsTotal = catalog.calc_products_total(productQty);
            double boxesTotal = catalog.calc_boxes_total(boxQty);

            boolean canCheckout;
            String message;

            // regula finala:
            // - daca exista boxes -> checkout permis (chiar daca ai produse sub 100)
            // - daca ai doar produse -> minim 100
            if (hasBoxes) {
                canCheckout = true;
                message = hasProducts
                        ? "You can checkout (boxes in cart)."
                        : "You can checkout (boxes only).";
            } else {
                if (productsTotal >= MIN_SOLO_PRODUCTS) {
                    canCheckout = true;
                    message = "You can checkout (individual products >= 100 lei).";
                } else {
                    canCheckout = false;
                    double missing = MIN_SOLO_PRODUCTS - productsTotal;
                    message = "Minimum order for individual products is 100 lei. Add " + String.format("%.2f", missing) + " lei more.";
                }
            }

            double delivery = canCheckout ? DELIVERY_FEE : 0.0;
            double total = productsTotal + boxesTotal + delivery;

            return ResponseEntity.ok(new CheckoutResponse(canCheckout, message, productsTotal, boxesTotal, delivery, total));

        } catch (BulkOrderException e) {
            return ResponseEntity.badRequest().body(
                    new CheckoutResponse(false, e.getMessage(), 0, 0, 0, 0)
            );
        }
    }
}
