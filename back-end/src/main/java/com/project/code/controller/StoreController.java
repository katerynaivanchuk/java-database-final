package com.project.code.controller;

import com.project.code.model.PlaceOrderRequestDTO;
import com.project.code.model.Store;
import com.project.code.repo.StoreRepository;
import com.project.code.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StoreController {

    private final StoreRepository storeRepository;
    private final OrderService orderService;

    public StoreController(
            StoreRepository storeRepository,
            OrderService orderService
    ) {
        this.storeRepository = storeRepository;
        this.orderService = orderService;
    }

    @PostMapping("/store")
    public Map<String, String> addStore(@RequestBody Store store) {
        storeRepository.save(store);

        Map<String, String> response = new HashMap<>();
        response.put(
                "message",
                "Store " + store.getId() + " has been added successfully"
        );

        return response;
    }

    @GetMapping("/validate/store/{id}")
    public boolean validateStore(@PathVariable Long id) {
        return storeRepository.existsById(id);
    }

    @PostMapping("/store/placeOrder")
    public Map<String, String> placeOrder(
            @RequestBody PlaceOrderRequestDTO order
    ) {
        Map<String, String> response = new HashMap<>();

        try {
            orderService.saveOrder(order);
            response.put("message", "Order placed successfully");
        } catch (Exception e) {
            response.put("Error", e.getMessage());
        }

        return response;
    }
}
