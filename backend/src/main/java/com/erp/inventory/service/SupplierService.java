package com.erp.inventory.service;

import com.erp.inventory.dto.SupplierRequest;
import com.erp.inventory.dto.SupplierResponse;
import com.erp.inventory.entity.Supplier;
import com.erp.inventory.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {

        if (supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
            throw new RuntimeException("Supplier code already exists");
        }

        if (supplierRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Supplier email already exists");
        }

        Supplier supplier = new Supplier();

        supplier.setSupplierCode(request.getSupplierCode());
        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        supplier.setStatus(request.getStatus());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return toResponse(savedSupplier);
    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {

        return supplierRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return toResponse(supplier);
    }

    @Transactional(readOnly = true)
    public SupplierResponse getSupplierByCode(String supplierCode) {

        Supplier supplier = supplierRepository.findBySupplierCode(supplierCode)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        return toResponse(supplier);
    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> getSuppliersByStatus(String status) {

        return supplierRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SupplierResponse updateSupplier(
            Long id,
            SupplierRequest request) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (!supplier.getSupplierCode().equals(request.getSupplierCode())
                && supplierRepository.existsBySupplierCode(request.getSupplierCode())) {

            throw new RuntimeException("Supplier code already exists");
        }

        if (!supplier.getEmail().equals(request.getEmail())
                && supplierRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException("Supplier email already exists");
        }

        supplier.setSupplierCode(request.getSupplierCode());
        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        supplier.setStatus(request.getStatus());

        Supplier updatedSupplier = supplierRepository.save(supplier);

        return toResponse(updatedSupplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplierRepository.delete(supplier);
    }

    private SupplierResponse toResponse(Supplier supplier) {

        return new SupplierResponse(
                supplier.getId(),
                supplier.getSupplierCode(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getStatus()
        );
    }
}