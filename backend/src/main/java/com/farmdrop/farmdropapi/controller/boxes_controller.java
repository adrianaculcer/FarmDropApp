package com.farmdrop.farmdropapi.controller;

import com.farmdrop.farmdropapi.dto.BoxDto;
import com.farmdrop.farmdropapi.service.catalog_service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class boxes_controller {

    private final catalog_service catalog;

    public boxes_controller(catalog_service catalog) {
        this.catalog = catalog;
    }

    @GetMapping("/api/boxes")
    public List<BoxDto> get_boxes() {
        return catalog.get_boxes_dto();
    }
}
