package com.project.code.controller;

import com.project.code.exceptions.StoreNotFoundException;
import com.project.code.model.*;
import com.project.code.repo.StoreRepository;
import com.project.code.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to designate it as a REST controller for handling HTTP requests.
//    - Map the class to the `/store` URL using `@RequestMapping("/store")`.


 // 2. Autowired Dependencies:
//    - Inject the following dependencies via `@Autowired`:
//        - `StoreRepository` for managing store data.
//        - `OrderService` for handling order-related functionality.

    private final StoreRepository storeRepository;
    private final OrderService orderService;

    public StoreController(StoreRepository storeRepository, OrderService orderService) {
        this.storeRepository = storeRepository;
        this.orderService = orderService;
    }

    // 3. Define the `addStore` Method:
//    - Annotate with `@PostMapping` to create an endpoint for adding a new store.
//    - Accept `Store` object in the request body.
//    - Return a success message in a `Map<String, String>` with the key `message` containing store creation confirmation.

    @PostMapping()
    public Map<String, String> addStore(@RequestBody Store store) {
        storeRepository.save(store);
        Map<String, String> response = new HashMap<>();
        String message = "Store " + store.getId() + " has been added successfully";
        response.put("message", message);
        return response;
    }

 // 4. Define the `validateStore` Method:
//    - Annotate with `@GetMapping("validate/{storeId}")` to check if a store exists by its `storeId`.
//    - Return a **boolean** indicating if the store exists.

    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable("storeId") Long storeId) {
        return storeRepository.existsById(storeId);
    }

 // 5. Define the `placeOrder` Method:
//    - Annotate with `@PostMapping("/placeOrder")` to handle order placement.
//    - Accept `PlaceOrderRequestDTO` in the request body.
//    - Return a success message with key `message` if the order is successfully placed.
//    - Return an error message with key `Error` if there is an issue processing the order.

    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO order) {
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
