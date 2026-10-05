package com.erp.inventory.repository;

import com.erp.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Optional<Supplier> findBySupplierCode(String supplierCode);

    Optional<Supplier> findByEmail(String email);

    boolean existsBySupplierCode(String supplierCode);

    boolean existsByEmail(String email);

    List<Supplier> findByStatus(String status);
}