package com.project.code.repo;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
// 1. Add the repository interface:
//    - Extend JpaRepository<Product, Long> to inherit basic CRUD functionality.
//    - This allows the repository to perform operations like save, delete, update, and find without having to implement these methods manually.

// Example: public interface ProductRepository extends JpaRepository<Product, Long> {}

// 2. Add custom query methods:
//    - **findAll**:
//      - This method will retrieve all products.
//      - Return type: List<Product>

    
public List<Product> findAll();

// Example: public List<Product> findAll();

//    - **findByCategory**:
//      - This method will retrieve products by their category.
//      - Return type: List<Product>
//      - Parameter: String category

    public List<Product> findByCategory(String category);

// Example: public List<Product> findByCategory(String category);

//    - **findByPriceBetween**:
//      - This method will retrieve products within a price range.
//      - Return type: List<Product>
//      - Parameters: Double minPrice, Double maxPrice

    public List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

// Example: public List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

//    - **findBySku**:
//      - This method will retrieve a product by its SKU.
//      - Return type: Product
//      - Parameter: String sku

    public Product findBySku(String sku);

// Example: public Product findBySku(String sku);

//    - **findByName**:
//      - This method will retrieve a product by its name.
//      - Return type: Product
//      - Parameter: String name

    public Product findByName(String name);
// Example: public Product findByName(String name);

//    - **findByNameLike**:
//      - This method will retrieve products by a name pattern for a specific store.
//      - Return type: List<Product>
//      - Parameters: Long storeId, String pname
//      - Use @Query annotation to write a custom query.

    @Query("""
        SELECT * FROM PRODUCT p 
        WHERE p.store.id = :storeId AND p.name LIKE CONCAT('%', :pname, '%')"
    """)
    public List<Product> findByNameLike(@Param("storeId") Long storeId, @Param("pname") String pname);
    
    public Product findById(Long id);

    public Product findByName(String name);
    
}
