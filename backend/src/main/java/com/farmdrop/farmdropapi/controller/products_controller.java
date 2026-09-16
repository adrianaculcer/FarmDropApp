package com.farmdrop.farmdropapi.controller;

import com.farmdrop.farmdropapi.dto.ProductDto;
import com.farmdrop.farmdropapi.service.catalog_service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class products_controller {

    private final catalog_service catalog;

    public products_controller(catalog_service catalog) {
        this.catalog = catalog;
    }

    @GetMapping("/api/products")
    public List<ProductDto> get_products() {
        return catalog.get_products_dto();
    }
}
