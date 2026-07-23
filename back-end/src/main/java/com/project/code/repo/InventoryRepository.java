package com.project.code.repo;

import com.project.code.model.Inventory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    @Query("""
           SELECT i
           FROM Inventory i
           WHERE i.product.id = :productId
             AND i.store.id = :storeId
           """)
    Inventory findByProduct_IdAndStore_Id(
            @Param("productId") Long productId,
            @Param("storeId") Long storeId
    );

    List<Inventory> findByStore_Id(Long storeId);

    @Modifying
    @Transactional
    void deleteByProduct_Id(Long productId);
}
