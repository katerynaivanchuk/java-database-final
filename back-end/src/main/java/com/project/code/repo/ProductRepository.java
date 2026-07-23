package com.project.code.repo;

import com.project.code.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    Product findBySku(String sku);

    Product findByName(String name);

    @Query("""
           SELECT i.product
           FROM Inventory i
           WHERE i.store.id = :storeId
             AND LOWER(i.product.name)
                 LIKE LOWER(CONCAT('%', :pname, '%'))
           """)
    List<Product> findByNameLike(
            @Param("storeId") Long storeId,
            @Param("pname") String pname
    );

    @Query("""
           SELECT i.product
           FROM Inventory i
           WHERE i.store.id = :storeId
             AND i.product.category = :category
           """)
    List<Product> findByCategoryAndStore_Id(
            @Param("storeId") Long storeId,
            @Param("category") String category
    );

    @Query("""
           SELECT i.product
           FROM Inventory i
           WHERE i.store.id = :storeId
             AND LOWER(i.product.name)
                 LIKE LOWER(CONCAT('%', :pname, '%'))
             AND i.product.category = :category
           """)
    List<Product> findByNameAndCategory(
            @Param("storeId") Long storeId,
            @Param("pname") String pname,
            @Param("category") String category
    );

    @Query("""
           SELECT i.product
           FROM Inventory i
           WHERE i.store.id = :storeId
           """)
    List<Product> findProductsByStore_Id(
            @Param("storeId") Long storeId
    );

    List<Product> findProductsByNameAndCategory(
            String name,
            String category
    );

    @Query("""
           SELECT p
           FROM Product p
           WHERE LOWER(p.name)
                 LIKE LOWER(CONCAT('%', :name, '%'))
           """)
    List<Product> findProductBySubName(
            @Param("name") String name
    );
}
