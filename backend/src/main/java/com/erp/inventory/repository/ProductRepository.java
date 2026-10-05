package com.erp.inventory.repository;

import com.erp.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

    boolean existsByProductCode(String productCode);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByStatus(String status);

    List<Product> findByQuantityLessThanEqual(Integer quantity);
    Page<Product> findAllByOrderByIdDesc(Pageable pageable);

    // =========================================================
    // REDUCE STOCK
    // =========================================================

    @Modifying
    @Query("""
        UPDATE Product p
        SET p.quantity = p.quantity - :quantity
        WHERE p.id = :productId
        AND p.quantity >= :quantity
    """)
    int reduceStock(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );

    // =========================================================
    // INCREASE / RESTORE STOCK
    // =========================================================

    @Modifying
    @Query("""
        UPDATE Product p
        SET p.quantity = p.quantity + :quantity
        WHERE p.id = :productId
    """)
    int increaseStock(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );
}
