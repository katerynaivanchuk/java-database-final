package com.project.code.service;
import com.project.code.exceptions.*;
import com.project.code.model.*;
import com.project.code.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

// 1. **saveOrder Method**:
//    - Processes a customer's order, including saving the order details and associated items.
//    - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for placing an order)
//    - Return Type: `void` (This method doesn't return anything, it just processes the order)

public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {
    Customer customer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());

    if(customer == null) {
        customer = new Customer(placeOrderRequest.getCustomerName(), placeOrderRequest.getCustomerEmail(), placeOrderRequest.getCustomerPhone());
        customerRepository.save(customer);
    }

    Store store = storeRepository.findById(placeOrderRequest.getStoreId())
            .orElseThrow(() -> new StoreNotFoundException(placeOrderRequest.getStoreId()));


    OrderDetails orderDetails = new OrderDetails(customer,store, placeOrderRequest.getTotalPrice(), java.time.LocalDateTime.now());
    orderDetailsRepository.save(orderDetails);

    for(PurchaseProductDTO dto: placeOrderRequest.getPurchaseProduct()) {
        Inventory inventory = inventoryRepository.findByProduct_IdAndStore_Id(dto.getId(), store.getId());

        Integer stockLevel = inventory.getStockLevel();
        Integer newStockLevel = stockLevel - dto.getQuantity();
        inventory.setStockLevel(newStockLevel);
        inventoryRepository.save(inventory);

        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new ProductNotFoundException(dto.getId()));

        OrderItem orderItem = new OrderItem(orderDetails, product, dto.getQuantity(), dto.getPrice());
        orderItemRepository.save(orderItem);
    }

}

// 2. **Retrieve or Create the Customer**:
//    - Check if the customer exists by their email using `findByEmail`.
//    - If the customer exists, use the existing customer; otherwise, create and save a new customer using `customerRepository.save()`.

// 3. **Retrieve the Store**:
//    - Fetch the store by ID from `storeRepository`.
//    - If the store doesn't exist, throw an exception. Use `storeRepository.findById()`.

// 4. **Create OrderDetails**:
//    - Create a new `OrderDetails` object and set customer, store, total price, and the current timestamp.
//    - Set the order date using `java.time.LocalDateTime.now()` and save the order with `orderDetailsRepository.save()`.

// 5. **Create and Save OrderItems**:
//    - For each product purchased, find the corresponding inventory, update stock levels, and save the changes using `inventoryRepository.save()`.
//    - Create and save `OrderItem` for each product and associate it with the `OrderDetails` using `orderItemRepository.save()`.

   
}
