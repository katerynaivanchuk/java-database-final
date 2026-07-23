package com.project.code.service;

import com.project.code.exceptions.ProductNotFoundException;
import com.project.code.exceptions.StoreNotFoundException;
import com.project.code.model.Customer;
import com.project.code.model.Inventory;
import com.project.code.model.OrderDetails;
import com.project.code.model.OrderItem;
import com.project.code.model.PlaceOrderRequestDTO;
import com.project.code.model.Product;
import com.project.code.model.PurchaseProductDTO;
import com.project.code.model.Store;
import com.project.code.repo.CustomerRepository;
import com.project.code.repo.InventoryRepository;
import com.project.code.repo.OrderDetailsRepository;
import com.project.code.repo.OrderItemRepository;
import com.project.code.repo.ProductRepository;
import com.project.code.repo.StoreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            StoreRepository storeRepository,
            OrderDetailsRepository orderDetailsRepository,
            InventoryRepository inventoryRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {

        Customer customer =
                customerRepository.findByEmail(
                        placeOrderRequest.getCustomerEmail()
                );

        if (customer == null) {
            customer = new Customer(
                    placeOrderRequest.getCustomerName(),
                    placeOrderRequest.getCustomerEmail(),
                    placeOrderRequest.getCustomerPhone()
            );

            customerRepository.save(customer);
        }

        Store store = storeRepository
                .findById(placeOrderRequest.getStoreId())
                .orElseThrow(
                        () -> new StoreNotFoundException(
                                placeOrderRequest.getStoreId()
                        )
                );

        OrderDetails orderDetails = new OrderDetails(
                customer,
                store,
                placeOrderRequest.getTotalPrice(),
                LocalDateTime.now()
        );

        OrderDetails savedOrderDetails =
                orderDetailsRepository.save(orderDetails);

        for (PurchaseProductDTO dto
                : placeOrderRequest.getPurchaseProduct()) {

            Inventory inventory =
                    inventoryRepository.findByProduct_IdAndStore_Id(
                            dto.getId(),
                            store.getId()
                    );

            if (inventory == null) {
                throw new ProductNotFoundException(dto.getId());
            }

            int newStockLevel =
                    inventory.getStockLevel() - dto.getQuantity();

            inventory.setStockLevel(newStockLevel);
            inventoryRepository.save(inventory);

            Product product = productRepository
                    .findById(dto.getId())
                    .orElseThrow(
                            () -> new ProductNotFoundException(dto.getId())
                    );

            OrderItem orderItem = new OrderItem(
                    savedOrderDetails,
                    product,
                    dto.getQuantity(),
                    dto.getPrice()
            );

            orderItemRepository.save(orderItem);
        }
    }
}
