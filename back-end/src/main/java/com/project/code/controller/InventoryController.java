package com.project.code.controller;

import com.project.code.exceptions.*;
import com.project.code.model.CombinedRequest;
import com.project.code.model.Inventory;
import com.project.code.model.Product;
import com.project.code.repo.InventoryRepository;
import com.project.code.repo.ProductRepository;
import com.project.code.service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to indicate that this is a REST controller, which handles HTTP requests and responses.
//    - Use `@RequestMapping("/inventory")` to set the base URL path for all methods in this controller. All endpoints related to inventory will be prefixed with `/inventory`.


// 2. Autowired Dependencies:
//    - Autowire necessary repositories and services:
//      - `ProductRepository` will be used to interact with product data (i.e., finding, updating products).
//      - `InventoryRepository` will handle CRUD operations related to the inventory.
//      - `ServiceClass` will help with the validation logic (e.g., validating product IDs and inventory data).


    private final ProductRepository productRepository;

    private final InventoryRepository inventoryRepository;

    private final ServiceClass serviceClass;

    public InventoryController(ProductRepository productRepository, InventoryRepository inventoryRepository, ServiceClass serviceClass) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.serviceClass = serviceClass;
    }

// 3. Define the `updateInventory` Method:
//    - This method handles HTTP PUT requests to update inventory for a product.
//    - It takes a `CombinedRequest` (containing `Product` and `Inventory`) in the request body.
//    - The product ID is validated, and if valid, the inventory is updated in the database.
//    - If the inventory exists, update it and return a success message. If not, return a message indicating no data available.

    @PutMapping()
    public Map<String,String> updateInventory(@RequestBody CombinedRequest request) {
        Map<String, String> response = new HashMap<>();
        try{


            Product product = request.getProduct();
            Inventory inventory = request.getInventory();

            boolean productValidation = serviceClass.validateProductId(product.getId());
            if(!productValidation) {
                response.put("message", "Product doesn't exist");
                return response;
            }

            Inventory existingInventory = inventoryRepository.findByProduct_IdAndStore_Id(product.getId(), inventory.getStore().getId());

            if(existingInventory == null) {
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
            return response;
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
        }


        return response;
    }

// 4. Define the `saveInventory` Method:
//    - This method handles HTTP POST requests to save a new inventory entry.
//    - It accepts an `Inventory` object in the request body.
//    - It first validates whether the inventory already exists. If it exists, it returns a message stating so. If it doesn’t exist, it saves the inventory and returns a success message.

    @PostMapping()
    public Map<String,String> saveInventory(Inventory inventory) {
        Map<String, String> response = new HashMap<>();
        try {

            if(!serviceClass.validateInventory(inventory)) {
                response.put("message", "The data is already present");
                return response;
            } else {
                inventoryRepository.save(inventory);
                response.put("message", "Data saved successfully");
                return response;
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

// 5. Define the `getAllProducts` Method:
//    - This method handles HTTP GET requests to retrieve products for a specific store.
//    - It uses the `storeId` as a path variable and fetches the list of products from the database for the given store.
//    - The products are returned in a `Map` with the key `"products"`.

    @GetMapping("/storeid")
    public Map<String, Object> getAllProducts(@PathVariable Long storeid) {
        List<Product> products = productRepository.findProductsByStore_Id(storeid);
        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        return response;
    }


// 6. Define the `getProductName` Method:
//    - This method handles HTTP GET requests to filter products by category and name.
//    - If either the category or name is `"null"`, adjust the filtering logic accordingly.
//    - Return the filtered products in the response with the key `"product"`.

    @GetMapping("filter/{category}/{name}/{storeid}")
    public Map<String, Object> getProductName(@PathVariable String category,
                                              @PathVariable String name,
                                              @PathVariable Long storeid) {
        Map<String, Object> response = new HashMap<>();

        if(category == null) {
            List<Product> products = productRepository.findByNameLike(storeid, name);
            response.put("products", products);
        } else if(name==null) {
            List<Product> products = productRepository.findByCategoryAndStore_Id(storeid, category);
            response.put("products", products);
        } else {
            List<Product> products = productRepository.findByNameAndCategory(storeid, name, category);
            response.put("products", products);
        }

        return response;
    }

// 7. Define the `searchProduct` Method:
//    - This method handles HTTP GET requests to search for products by name within a specific store.
//    - It uses `name` and `storeId` as parameters and searches for products that match the `name` in the specified store.
//    - The search results are returned in the response with the key `"product"`.

        @GetMapping("search/{name}/{storeId}")
        public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable Long storeid) {
            Map<String, Object> response = new HashMap<>();

            List<Product> products = productRepository.findByNameLike(storeid, name);
            response.put("products", products);
            return response;
        }

// 8. Define the `removeProduct` Method:
//    - This method handles HTTP DELETE requests to delete a product by its ID.
//    - It first validates if the product exists. If it does, it deletes the product from the `ProductRepository` and also removes the related inventory entry from the `InventoryRepository`.
//    - Returns a success message with the key `"message"` indicating successful deletion.

        @DeleteMapping("/{id}")
        public Map<String, String> removeProduct(@PathVariable Long id) {
            Map<String, String> response = new HashMap<>();
            if (serviceClass.validateProductId(id)) {
                inventoryRepository.deleteByProductId(id);
                response.put("message", "Successfully removed product");
            } else {
                response.put("message", "Product doesn't exist");
            }
            return response;
        }

// 9. Define the `validateQuantity` Method:
//    - This method handles HTTP GET requests to validate if a specified quantity of a product is available in stock for a given store.
//    - It checks the inventory for the product in the specified store and compares it to the requested quantity.
//    - If sufficient stock is available, return `true`; otherwise, return `false`.

        @GetMapping("validate/{quantity}/{storeId}/{productId}")
        public boolean validateProduct(@PathVariable Long quantity, @PathVariable Long storeId, @PathVariable Long productId) {
            Inventory inventory = inventoryRepository.findByProduct_IdAndStore_Id(productId, storeId);

            return inventory.getStockLevel() >= quantity;
        }
}
