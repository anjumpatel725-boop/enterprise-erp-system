package com.erp.sales.service;

import com.erp.audit.service.AuditLogService;
import com.erp.auth.entity.User;
import com.erp.auth.repository.UserRepository;
import com.erp.inventory.entity.Product;
import com.erp.inventory.repository.ProductRepository;
import com.erp.sales.dto.SalesOrderItemRequest;
import com.erp.sales.dto.SalesOrderRequest;
import com.erp.sales.dto.SalesOrderResponse;
import com.erp.sales.entity.Customer;
import com.erp.sales.entity.SalesOrder;
import com.erp.sales.repository.CustomerRepository;
import com.erp.sales.repository.SalesOrderItemRepository;
import com.erp.sales.repository.SalesOrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesOrderServiceImplTest {

    @Mock
    private SalesOrderRepository salesOrderRepository;

    @Mock
    private SalesOrderItemRepository salesOrderItemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private SalesOrderServiceImpl salesOrderService;

    private Customer customer;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        ReflectionTestUtils.setField(
                customer,
                "id",
                1L
        );

        user = new User();
        ReflectionTestUtils.setField(
                user,
                "id",
                1L
        );

        ReflectionTestUtils.setField(
                user,
                "username",
                "anjum"
        );

        product = new Product();
        ReflectionTestUtils.setField(
                product,
                "id",
                2L
        );

        product.setName(
                "Dell Latitude Laptop"
        );

        product.setStatus(
                "ACTIVE"
        );

        product.setQuantity(
                10
        );
    }

    // =========================================================
    // TEST 1
    // CREATE ORDER SUCCESS
    // =========================================================

    @Test
    void createOrder_shouldCreateOrderSuccessfully() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        SalesOrderRequest request =
                new SalesOrderRequest();

        request.setCustomerId(1L);
        request.setShippingAddress("MG Road");
        request.setNotes("Test order");
        request.setPaymentStatus("UNPAID");
        request.setStatus("PENDING");

        SalesOrderItemRequest itemRequest =
                new SalesOrderItemRequest();

        itemRequest.setProductId(2L);
        itemRequest.setProductName(
                "Dell Latitude Laptop"
        );
        itemRequest.setQuantity(2);
        itemRequest.setUnitPrice(
                new BigDecimal("60000")
        );

        request.setItems(
                List.of(itemRequest)
        );

        // Customer exists
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        // User exists
        when(userRepository.findByUsername("anjum"))
                .thenReturn(Optional.of(user));

        // Product exists
        when(productRepository.findById(2L))
                .thenReturn(Optional.of(product));

        /*
         * VERY IMPORTANT:
         * reduceStock() returns number of updated rows.
         *
         * 1 = successful stock reduction.
         */
        when(productRepository.reduceStock(2L, 2))
                .thenReturn(1);

        // Save order
        when(salesOrderRepository.save(
                any(SalesOrder.class)
        )).thenAnswer(invocation -> {

            SalesOrder order =
                    invocation.getArgument(0);

            ReflectionTestUtils.setField(
                    order,
                    "id",
                    1L
            );

            return order;
        });

        // -----------------------------------------------------
        // Act
        // -----------------------------------------------------

        SalesOrderResponse response =
                salesOrderService.createOrder(
                        request,
                        "anjum"
                );

        // -----------------------------------------------------
        // Assert
        // -----------------------------------------------------

        assertNotNull(response);

        verify(customerRepository)
                .findById(1L);

        verify(userRepository)
                .findByUsername("anjum");

        verify(productRepository)
                .findById(2L);

        verify(productRepository)
                .reduceStock(2L, 2);

        verify(salesOrderRepository, atLeastOnce())
                .save(any(SalesOrder.class));

        verify(salesOrderItemRepository)
                .save(any());

        verify(auditLogService)
                .log(
                        eq("anjum"),
                        eq("CREATE"),
                        eq("SALES"),
                        eq("SalesOrder"),
                        eq(1L),
                        contains("Sales order created")
                );

        assertEquals(
                new BigDecimal("120000"),
                response.getTotalAmount()
        );
    }

    // =========================================================
    // TEST 2
    // CUSTOMER NOT FOUND
    // =========================================================

    @Test
    void createOrder_shouldThrowExceptionWhenCustomerNotFound() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        SalesOrderRequest request =
                new SalesOrderRequest();

        request.setCustomerId(999L);

        SalesOrderItemRequest itemRequest =
                new SalesOrderItemRequest();

        itemRequest.setProductId(2L);
        itemRequest.setQuantity(2);
        itemRequest.setUnitPrice(
                new BigDecimal("60000")
        );

        request.setItems(
                List.of(itemRequest)
        );

        when(customerRepository.findById(999L))
                .thenReturn(Optional.empty());

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                salesOrderService.createOrder(
                                        request,
                                        "anjum"
                                )
                );

        assertEquals(
                "Customer not found with id: 999",
                exception.getMessage()
        );

        verify(customerRepository)
                .findById(999L);

        /*
         * Since customer was not found,
         * execution must NOT reach user/product repositories.
         */
        verifyNoInteractions(
                userRepository,
                productRepository,
                salesOrderRepository,
                salesOrderItemRepository,
                auditLogService
        );
    }

    // =========================================================
    // TEST 3
    // PRODUCT NOT FOUND
    // =========================================================

    @Test
    void createOrder_shouldThrowExceptionWhenProductNotFound() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        SalesOrderRequest request =
                new SalesOrderRequest();

        request.setCustomerId(1L);
        request.setPaymentStatus("UNPAID");
        request.setStatus("PENDING");

        SalesOrderItemRequest itemRequest =
                new SalesOrderItemRequest();

        itemRequest.setProductId(999L);
        itemRequest.setQuantity(2);
        itemRequest.setUnitPrice(
                new BigDecimal("60000")
        );

        request.setItems(
                List.of(itemRequest)
        );

        // Customer MUST exist first
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        // User MUST exist second
        when(userRepository.findByUsername("anjum"))
                .thenReturn(Optional.of(user));

        // Product does NOT exist
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Save order before product lookup
        when(salesOrderRepository.save(
                any(SalesOrder.class)
        )).thenAnswer(invocation -> {

            SalesOrder order =
                    invocation.getArgument(0);

            ReflectionTestUtils.setField(
                    order,
                    "id",
                    1L
            );

            return order;
        });

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                salesOrderService.createOrder(
                                        request,
                                        "anjum"
                                )
                );

        assertEquals(
                "Product not found with id: 999",
                exception.getMessage()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(customerRepository)
                .findById(1L);

        verify(userRepository)
                .findByUsername("anjum");

        verify(productRepository)
                .findById(999L);

        verify(productRepository, never())
                .reduceStock(
                        anyLong(),
                        anyInt()
                );

        verify(auditLogService, never())
                .log(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyLong(),
                        anyString()
                );
    }

    // =========================================================
    // TEST 4
    // INSUFFICIENT STOCK
    // =========================================================

    @Test
    void createOrder_shouldThrowExceptionWhenStockIsInsufficient() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        SalesOrderRequest request =
                new SalesOrderRequest();

        request.setCustomerId(1L);
        request.setPaymentStatus("UNPAID");
        request.setStatus("PENDING");

        SalesOrderItemRequest itemRequest =
                new SalesOrderItemRequest();

        itemRequest.setProductId(2L);

        /*
         * Product has only 10 stock.
         * Requesting 20.
         */
        itemRequest.setQuantity(20);

        itemRequest.setUnitPrice(
                new BigDecimal("60000")
        );

        request.setItems(
                List.of(itemRequest)
        );

        // Customer exists
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        // User exists
        when(userRepository.findByUsername("anjum"))
                .thenReturn(Optional.of(user));

        // Product exists with only 10 stock
        product.setQuantity(10);

        when(productRepository.findById(2L))
                .thenReturn(Optional.of(product));

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                salesOrderService.createOrder(
                                        request,
                                        "anjum"
                                )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Insufficient stock")
        );

        assertTrue(
                exception.getMessage()
                        .contains("Dell Latitude Laptop")
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(customerRepository)
                .findById(1L);

        verify(userRepository)
                .findByUsername("anjum");

        verify(productRepository)
                .findById(2L);

        /*
         * Stock check fails BEFORE reduceStock().
         */
        verify(productRepository, never())
                .reduceStock(
                        anyLong(),
                        anyInt()
                );

        verify(salesOrderItemRepository, never())
                .save(any());

        verify(auditLogService, never())
                .log(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyLong(),
                        anyString()
                );
    }
}