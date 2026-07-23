package com.project.code.service;

import com.project.code.model.Inventory;
import com.project.code.model.Product;
import com.project.code.repo.InventoryRepository;
import com.project.code.repo.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ServiceClass(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository
    ) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public boolean validateInventory(Inventory inventory) {
        Inventory existingInventory =
                inventoryRepository.findByProduct_IdAndStore_Id(
                        inventory.getProduct().getId(),
                        inventory.getStore().getId()
                );

        return existingInventory == null;
    }

    public boolean validateProduct(Product product) {
        return productRepository.findByName(product.getName()) == null;
    }

    public boolean validateProductId(Long id) {
        return productRepository.findById(id).isPresent();
    }

    public Inventory getInventoryId(Inventory inventory) {
        return inventoryRepository.findByProduct_IdAndStore_Id(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );
    }
}
