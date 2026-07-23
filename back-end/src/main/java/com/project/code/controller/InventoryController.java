package com.project.code.controller;

import com.project.code.model.CombinedRequest;
import com.project.code.model.Inventory;
import com.project.code.model.Product;
import com.project.code.repo.InventoryRepository;
import com.project.code.repo.ProductRepository;
import com.project.code.service.ServiceClass;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ServiceClass serviceClass;

    public InventoryController(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            ServiceClass serviceClass
    ) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.serviceClass = serviceClass;
    }

    @PutMapping
    public Map<String, String> updateInventory(
            @RequestBody CombinedRequest request
    ) {
        Map<String, String> response = new HashMap<>();

        try {
            Product product = request.getProduct();
            Inventory inventory = request.getInventory();

            boolean productExists =
                    serviceClass.validateProductId(product.getId());

            if (!productExists) {
                response.put("message", "Product doesn't exist");
                return response;
            }

            Inventory existingInventory =
                    inventoryRepository.findByProduct_IdAndStore_Id(
                            product.getId(),
                            inventory.getStore().getId()
                    );

            if (existingInventory == null) {
                response.put("message", "No data available");
                return response;
            }

            Product existingProduct = existingInventory.getProduct();

            existingProduct.setName(product.getName());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setSku(product.getSku());

            productRepository.save(existingProduct);

            existingInventory.setStockLevel(inventory.getStockLevel());
            inventoryRepository.save(existingInventory);

            response.put("message", "Successfully updated product");

        } catch (DataIntegrityViolationException e) {
            response.put("message", e.getMessage());
        }

        return response;
    }

    @PostMapping
    public Map<String, String> saveInventory(
            @RequestBody Inventory inventory
    ) {
        Map<String, String> response = new HashMap<>();

        try {
            if (serviceClass.validateInventory(inventory)) {
                response.put("message", "The data is already present");
            } else {
                inventoryRepository.save(inventory);
                response.put("message", "Data saved successfully");
            }

        } catch (DataIntegrityViolationException e) {
            response.put("message", e.getMessage());
        }

        return response;
    }

    @GetMapping("/store/{storeId}")
    public Map<String, Object> getAllProducts(
            @PathVariable("storeId") Long storeId
    ) {
        List<Product> products =
                productRepository.findProductsByStore_Id(storeId);

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);

        return response;
    }

    @GetMapping("/filter/{category}/{name}/{storeId}")
    public Map<String, Object> getProductName(
            @PathVariable("category") String category,
            @PathVariable("name") String name,
            @PathVariable("storeId") Long storeId
    ) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products;

        if ("null".equalsIgnoreCase(category)) {
            products = productRepository.findByNameLike(storeId, name);

        } else if ("null".equalsIgnoreCase(name)) {
            products = productRepository.findByCategoryAndStore_Id(
                    storeId,
                    category
            );

        } else {
            products = productRepository.findByNameAndCategory(
                    storeId,
                    name,
                    category
            );
        }

        response.put("products", products);
        return response;
    }

    @GetMapping("/search/{name}/{storeId}")
    public Map<String, Object> searchProduct(
            @PathVariable("name") String name,
            @PathVariable("storeId") Long storeId
    ) {
        List<Product> products =
                productRepository.findByNameLike(storeId, name);

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);

        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(
            @PathVariable("id") Long id
    ) {
        Map<String, String> response = new HashMap<>();

        if (serviceClass.validateProductId(id)) {
            inventoryRepository.deleteByProductId(id);
            response.put("message", "Successfully removed product");
        } else {
            response.put("message", "Product doesn't exist");
        }

        return response;
    }

    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(
            @PathVariable("quantity") Long quantity,
            @PathVariable("storeId") Long storeId,
            @PathVariable("productId") Long productId
    ) {
        Inventory inventory =
                inventoryRepository.findByProduct_IdAndStore_Id(
                        productId,
                        storeId
                );

        return inventory != null
                && inventory.getStockLevel() >= quantity;
    }
}
