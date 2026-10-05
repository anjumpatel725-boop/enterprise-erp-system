package com.erp.inventory.service;

import com.erp.inventory.dto.StockTransactionRequest;
import com.erp.inventory.dto.StockTransactionResponse;
import com.erp.inventory.entity.Product;
import com.erp.inventory.entity.StockTransaction;
import com.erp.inventory.entity.Supplier;
import com.erp.inventory.repository.ProductRepository;
import com.erp.inventory.repository.StockTransactionRepository;
import com.erp.inventory.repository.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockTransactionService {

    private final StockTransactionRepository stockTransactionRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public StockTransactionService(
            StockTransactionRepository stockTransactionRepository,
            ProductRepository productRepository,
            SupplierRepository supplierRepository) {

        this.stockTransactionRepository = stockTransactionRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public StockTransactionResponse createTransaction(
            StockTransactionRequest request) {

        String transactionType =
                request.getTransactionType().toUpperCase();

        if (!transactionType.equals("IN")
                && !transactionType.equals("OUT")) {

            throw new RuntimeException(
                    "Transaction type must be IN or OUT"
            );
        }

        Product product = productRepository.findById(
                request.getProductId()
        ).orElseThrow(
                () -> new RuntimeException("Product not found")
        );

        Supplier supplier = null;

        if (request.getSupplierId() != null) {

            supplier = supplierRepository.findById(
                    request.getSupplierId()
            ).orElseThrow(
                    () -> new RuntimeException("Supplier not found")
            );
        }

        /*
         * STOCK IN
         */
        if (transactionType.equals("IN")) {

            product.setQuantity(
                    product.getQuantity() + request.getQuantity()
            );
        }

        /*
         * STOCK OUT
         */
        if (transactionType.equals("OUT")) {

            if (product.getQuantity() < request.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock"
                );
            }

            product.setQuantity(
                    product.getQuantity() - request.getQuantity()
            );
        }

        productRepository.save(product);

        StockTransaction transaction =
                new StockTransaction();

        transaction.setProduct(product);
        transaction.setSupplier(supplier);
        transaction.setTransactionType(transactionType);
        transaction.setQuantity(request.getQuantity());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRemarks(request.getRemarks());

        StockTransaction savedTransaction =
                stockTransactionRepository.save(transaction);

        return toResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<StockTransactionResponse> getAllTransactions() {

        return stockTransactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StockTransactionResponse getTransactionById(Long id) {

        StockTransaction transaction =
                stockTransactionRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Stock transaction not found"
                                )
                        );

        return toResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<StockTransactionResponse> getTransactionsByProduct(
            Long productId) {

        if (!productRepository.existsById(productId)) {

            throw new RuntimeException("Product not found");
        }

        return stockTransactionRepository
                .findByProductId(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockTransactionResponse> getTransactionsByType(
            String transactionType) {

        String type = transactionType.toUpperCase();

        if (!type.equals("IN") && !type.equals("OUT")) {

            throw new RuntimeException(
                    "Transaction type must be IN or OUT"
            );
        }

        return stockTransactionRepository
                .findByTransactionType(type)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private StockTransactionResponse toResponse(
            StockTransaction transaction) {

        Product product = transaction.getProduct();
        Supplier supplier = transaction.getSupplier();

        return new StockTransactionResponse(
                transaction.getId(),

                product.getId(),
                product.getProductCode(),
                product.getName(),

                supplier != null
                        ? supplier.getId()
                        : null,

                supplier != null
                        ? supplier.getName()
                        : null,

                transaction.getTransactionType(),
                transaction.getQuantity(),
                transaction.getTransactionDate(),
                transaction.getRemarks()
        );
    }
}