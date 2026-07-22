package com.project.code.service;

import com.project.code.model.Inventory;
import com.project.code.model.Product;
import com.project.code.repo.InventoryRepository;
import com.project.code.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ServiceClass(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }


// 1. **validateInventory Method**:
//    - Checks if an inventory record exists for a given product and store combination.
//    - Parameters: `Inventory inventory`
//    - Return Type: `boolean` (Returns `false` if inventory exists, otherwise `true`)

    public boolean validateInventory(Inventory inventory) {
        return inventoryRepository.findByProduct_IdAndStore_Id(
            inventory.getProduct().getId(),
            inventory.getStore().getId()) == null;
    }
// 2. **validateProduct Method**:
//    - Checks if a product exists by its name.
//    - Parameters: `Product product`
//    - Return Type: `boolean` (Returns `false` if a product with the same name exists, otherwise `true`)

    public boolean validateProduct(Product product) {
        return productRepository.findByName(product.getName()) == null;
    }

// 3. **ValidateProductId Method**:
//    - Checks if a product exists by its ID.
//    - Parameters: `long id`
//    - Return Type: `boolean` (Returns `false` if the product does not exist with the given ID, otherwise `true`)

    public boolean validateProductId(Long id) {

        return productRepository.findById(id).isPresent();
    }

// 4. **getInventoryId Method**:
//    - Fetches the inventory record for a given product and store combination.
//    - Parameters: `Inventory inventory`
//    - Return Type: `Inventory` (Returns the inventory record for the product-store combination)

    public Inventory getInventoryId(Inventory inventory) {
        return inventoryRepository.findByProduct_IdAndStore_Id(
            inventory.getProduct().getId(),
            inventory.getStore().getId());
    }
}
