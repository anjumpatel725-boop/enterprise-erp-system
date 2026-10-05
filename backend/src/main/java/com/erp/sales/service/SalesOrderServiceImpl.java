package com.erp.sales.service;

import com.erp.audit.service.AuditLogService;
import com.erp.auth.entity.User;
import com.erp.auth.repository.UserRepository;
import com.erp.inventory.entity.Product;
import com.erp.inventory.repository.ProductRepository;
import com.erp.sales.dto.SalesOrderItemRequest;
import com.erp.sales.dto.SalesOrderItemResponse;
import com.erp.sales.dto.SalesOrderRequest;
import com.erp.sales.dto.SalesOrderResponse;
import com.erp.sales.entity.Customer;
import com.erp.sales.entity.SalesOrder;
import com.erp.sales.entity.SalesOrderItem;
import com.erp.sales.entity.SalesOrderStatus;
import com.erp.sales.repository.CustomerRepository;
import com.erp.sales.repository.SalesOrderItemRepository;
import com.erp.sales.repository.SalesOrderRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public SalesOrderServiceImpl(
            SalesOrderRepository salesOrderRepository,
            SalesOrderItemRepository salesOrderItemRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            AuditLogService auditLogService) {

        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    // =========================================================
    // CREATE ORDER
    // =========================================================

    @Override
    public SalesOrderResponse createOrder(
            SalesOrderRequest request,
            String username) {

        if (request == null) {
            throw new RuntimeException(
                    "Order request cannot be null"
            );
        }

        if (request.getCustomerId() == null) {
            throw new RuntimeException(
                    "Customer ID is required"
            );
        }

        if (request.getItems() == null
                || request.getItems().isEmpty()) {

            throw new RuntimeException(
                    "Order must contain at least one item"
            );
        }

        Customer customer =
                customerRepository.findById(
                                request.getCustomerId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found with id: "
                                                + request.getCustomerId()
                                )
                        );

        User user =
                userRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found: "
                                                + username
                                )
                        );

        SalesOrder order =
                new SalesOrder();

        order.setOrderNumber(
                generateOrderNumber()
        );

        order.setCustomer(customer);

        order.setCreatedBy(user);

        order.setShippingAddress(
                request.getShippingAddress()
        );

        order.setNotes(
                request.getNotes()
        );

        if (request.getStatus() != null
                && !request.getStatus().isBlank()) {

            order.setStatus(
                    parseStatus(
                            request.getStatus()
                    )
            );

        } else {

            order.setStatus(
                    SalesOrderStatus.PENDING
            );
        }

        if (request.getPaymentStatus() != null) {

            order.setPaymentStatus(
                    request.getPaymentStatus()
            );
        }

        order.setTotalAmount(
                BigDecimal.ZERO
        );

        SalesOrder savedOrder =
                salesOrderRepository.save(order);

        BigDecimal total =
                BigDecimal.ZERO;

        for (SalesOrderItemRequest itemRequest :
                request.getItems()) {

            if (itemRequest == null) {

                throw new RuntimeException(
                        "Order item cannot be null"
                );
            }

            if (itemRequest.getProductId() == null) {

                throw new RuntimeException(
                        "Product ID is required"
                );
            }

            if (itemRequest.getQuantity() == null
                    || itemRequest.getQuantity() <= 0) {

                throw new RuntimeException(
                        "Quantity must be greater than zero"
                );
            }

            if (itemRequest.getUnitPrice() == null
                    || itemRequest.getUnitPrice()
                    .compareTo(BigDecimal.ZERO) < 0) {

                throw new RuntimeException(
                        "Unit price must be zero or greater"
                );
            }

            Product product =
                    productRepository.findById(
                                    itemRequest.getProductId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product not found with id: "
                                                    + itemRequest.getProductId()
                                    )
                            );

            if (!"ACTIVE".equalsIgnoreCase(
                    product.getStatus())) {

                throw new RuntimeException(
                        "Product is not active: "
                                + product.getName()
                );
            }

            if (product.getQuantity() == null
                    || product.getQuantity()
                    < itemRequest.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + product.getName()
                                + ". Available: "
                                + product.getQuantity()
                );
            }

            int updatedRows =
                    productRepository.reduceStock(
                            product.getId(),
                            itemRequest.getQuantity()
                    );

            if (updatedRows == 0) {

                throw new RuntimeException(
                        "Unable to reduce stock for product: "
                                + product.getName()
                );
            }

            SalesOrderItem item =
                    new SalesOrderItem();

            item.setSalesOrder(
                    savedOrder
            );

            item.setProductId(
                    product.getId()
            );

            item.setProductName(
                    product.getName()
            );

            item.setQuantity(
                    itemRequest.getQuantity()
            );

            item.setUnitPrice(
                    itemRequest.getUnitPrice()
            );

            BigDecimal subtotal =
                    itemRequest.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()
                                    )
                            );

            item.setSubtotal(
                    subtotal
            );

            salesOrderItemRepository.save(
                    item
            );

            total = total.add(subtotal);
        }

        savedOrder.setTotalAmount(
                total
        );

        SalesOrder updatedOrder =
                salesOrderRepository.save(
                        savedOrder
                );

        auditLogService.log(
                username,
                "CREATE",
                "SALES",
                "SalesOrder",
                updatedOrder.getId(),
                "Sales order created: "
                        + updatedOrder.getOrderNumber()
        );

        return mapToResponse(
                updatedOrder
        );
    }

    // =========================================================
    // GET ALL ORDERS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderResponse> getAllOrders() {

        List<SalesOrder> orders =
                salesOrderRepository.findAll();

        if (orders.isEmpty()) {

            return Collections.emptyList();
        }

        List<Long> orderIds =
                orders.stream()
                        .map(SalesOrder::getId)
                        .filter(Objects::nonNull)
                        .toList();

        List<SalesOrderItem> items =
                salesOrderItemRepository
                        .findBySalesOrderIds(
                                orderIds
                        );

        Map<Long, List<SalesOrderItem>> itemsByOrder =
                items.stream()
                        .collect(
                                Collectors.groupingBy(
                                        item ->
                                                item.getSalesOrder()
                                                        .getId()
                                )
                        );

        return orders.stream()
                .map(order ->
                        mapToResponse(
                                order,
                                itemsByOrder.getOrDefault(
                                        order.getId(),
                                        Collections.emptyList()
                                )
                        )
                )
                .toList();
    }

    // =========================================================
    // PAGINATED ORDERS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<SalesOrderResponse> getOrdersPaginated(
            int page,
            int size) {

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 10;
        }

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        Page<SalesOrder> orderPage =
                salesOrderRepository
                        .findAllByOrderByOrderDateDesc(
                                pageable
                        );

        List<SalesOrder> orders =
                orderPage.getContent();

        if (orders.isEmpty()) {

            return new PageImpl<>(
                    Collections.emptyList(),
                    pageable,
                    orderPage.getTotalElements()
            );
        }

        List<Long> orderIds =
                orders.stream()
                        .map(SalesOrder::getId)
                        .filter(Objects::nonNull)
                        .toList();

        List<SalesOrderItem> items =
                salesOrderItemRepository
                        .findBySalesOrderIds(
                                orderIds
                        );

        Map<Long, List<SalesOrderItem>> itemsByOrder =
                items.stream()
                        .collect(
                                Collectors.groupingBy(
                                        item ->
                                                item.getSalesOrder()
                                                        .getId()
                                )
                        );

        List<SalesOrderResponse> responses =
                orders.stream()
                        .map(order ->
                                mapToResponse(
                                        order,
                                        itemsByOrder.getOrDefault(
                                                order.getId(),
                                                Collections.emptyList()
                                        )
                                )
                        )
                        .toList();

        return new PageImpl<>(
                responses,
                pageable,
                orderPage.getTotalElements()
        );
    }

    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SalesOrderResponse getOrderById(
            Long id) {

        SalesOrder order =
                salesOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Sales order not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(
                order
        );
    }

    // =========================================================
    // UPDATE ORDER
    // =========================================================

    @Override
    public SalesOrderResponse updateOrder(
            Long id,
            SalesOrderRequest request) {

        if (request == null) {

            throw new RuntimeException(
                    "Order request cannot be null"
            );
        }

        if (request.getCustomerId() == null) {

            throw new RuntimeException(
                    "Customer ID is required"
            );
        }

        if (request.getItems() == null
                || request.getItems().isEmpty()) {

            throw new RuntimeException(
                    "Order must contain at least one item"
            );
        }

        SalesOrder order =
                salesOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Sales order not found with id: "
                                                + id
                                )
                        );

        Customer customer =
                customerRepository.findById(
                                request.getCustomerId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found with id: "
                                                + request.getCustomerId()
                                )
                        );

        /*
         * Restore old stock before replacing items.
         */
        List<SalesOrderItem> oldItems =
                salesOrderItemRepository
                        .findBySalesOrderId(id);

        for (SalesOrderItem oldItem :
                oldItems) {

            if (oldItem.getProductId() != null
                    && oldItem.getQuantity() != null
                    && oldItem.getQuantity() > 0) {

                productRepository.increaseStock(
                        oldItem.getProductId(),
                        oldItem.getQuantity()
                );
            }
        }

        /*
         * Delete old order items.
         */
        salesOrderItemRepository
                .deleteBySalesOrderId(id);

        order.setCustomer(
                customer
        );

        order.setShippingAddress(
                request.getShippingAddress()
        );

        order.setNotes(
                request.getNotes()
        );

        if (request.getStatus() != null
                && !request.getStatus().isBlank()) {

            order.setStatus(
                    parseStatus(
                            request.getStatus()
                    )
            );
        }

        if (request.getPaymentStatus() != null) {

            order.setPaymentStatus(
                    request.getPaymentStatus()
            );
        }

        BigDecimal total =
                BigDecimal.ZERO;

        for (SalesOrderItemRequest itemRequest :
                request.getItems()) {

            if (itemRequest == null) {

                throw new RuntimeException(
                        "Order item cannot be null"
                );
            }

            if (itemRequest.getProductId() == null) {

                throw new RuntimeException(
                        "Product ID is required"
                );
            }

            if (itemRequest.getQuantity() == null
                    || itemRequest.getQuantity() <= 0) {

                throw new RuntimeException(
                        "Quantity must be greater than zero"
                );
            }

            if (itemRequest.getUnitPrice() == null
                    || itemRequest.getUnitPrice()
                    .compareTo(BigDecimal.ZERO) < 0) {

                throw new RuntimeException(
                        "Unit price must be zero or greater"
                );
            }

            Product product =
                    productRepository.findById(
                                    itemRequest.getProductId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product not found with id: "
                                                    + itemRequest.getProductId()
                                    )
                            );

            if (!"ACTIVE".equalsIgnoreCase(
                    product.getStatus())) {

                throw new RuntimeException(
                        "Product is not active: "
                                + product.getName()
                );
            }

            if (product.getQuantity() == null
                    || product.getQuantity()
                    < itemRequest.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + product.getName()
                                + ". Available: "
                                + product.getQuantity()
                );
            }

            int updatedRows =
                    productRepository.reduceStock(
                            product.getId(),
                            itemRequest.getQuantity()
                    );

            if (updatedRows == 0) {

                throw new RuntimeException(
                        "Unable to reduce stock for product: "
                                + product.getName()
                );
            }

            SalesOrderItem item =
                    new SalesOrderItem();

            item.setSalesOrder(
                    order
            );

            item.setProductId(
                    product.getId()
            );

            item.setProductName(
                    product.getName()
            );

            item.setQuantity(
                    itemRequest.getQuantity()
            );

            item.setUnitPrice(
                    itemRequest.getUnitPrice()
            );

            BigDecimal subtotal =
                    itemRequest.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()
                                    )
                            );

            item.setSubtotal(
                    subtotal
            );

            salesOrderItemRepository.save(
                    item
            );

            total = total.add(
                    subtotal
            );
        }

        order.setTotalAmount(
                total
        );

        SalesOrder updatedOrder =
                salesOrderRepository.save(
                        order
                );

        auditLogService.log(
                getCurrentUsername(),
                "UPDATE",
                "SALES",
                "SalesOrder",
                updatedOrder.getId(),
                "Sales order updated: "
                        + updatedOrder.getOrderNumber()
        );

        return mapToResponse(
                updatedOrder
        );
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    @Override
    public SalesOrderResponse updateOrderStatus(
            Long id,
            String newStatus) {

        SalesOrder order =
                salesOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Sales order not found with id: "
                                                + id
                                )
                        );

        SalesOrderStatus currentStatus =
                order.getStatus();

        SalesOrderStatus targetStatus =
                parseStatus(
                        newStatus
                );

        if (currentStatus == targetStatus) {

            throw new RuntimeException(
                    "Order is already in status: "
                            + targetStatus
            );
        }

        if (!isValidStatusTransition(
                currentStatus,
                targetStatus)) {

            throw new RuntimeException(
                    "Invalid status transition from "
                            + currentStatus
                            + " to "
                            + targetStatus
            );
        }

        order.setStatus(
                targetStatus
        );

        SalesOrder updatedOrder =
                salesOrderRepository.save(
                        order
                );

        auditLogService.log(
                getCurrentUsername(),
                "STATUS_CHANGE",
                "SALES",
                "SalesOrder",
                updatedOrder.getId(),
                "Sales order status changed from "
                        + currentStatus
                        + " to "
                        + targetStatus
        );

        return mapToResponse(
                updatedOrder
        );
    }

    // =========================================================
    // DELETE ORDER
    // =========================================================

    @Override
    public void deleteOrder(
            Long id) {

        SalesOrder order =
                salesOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Sales order not found with id: "
                                                + id
                                )
                        );

        List<SalesOrderItem> oldItems =
                salesOrderItemRepository
                        .findBySalesOrderId(id);

        /*
         * Restore stock before deleting order.
         */
        for (SalesOrderItem item :
                oldItems) {

            if (item.getProductId() != null
                    && item.getQuantity() != null
                    && item.getQuantity() > 0) {

                productRepository.increaseStock(
                        item.getProductId(),
                        item.getQuantity()
                );
            }
        }

        /*
         * Delete child items first.
         */
        salesOrderItemRepository
                .deleteBySalesOrderId(id);

        /*
         * Delete parent order.
         */
        salesOrderRepository.delete(
                order
        );

        auditLogService.log(
                getCurrentUsername(),
                "DELETE",
                "SALES",
                "SalesOrder",
                id,
                "Sales order deleted: "
                        + order.getOrderNumber()
        );
    }

    // =========================================================
    // CURRENT USER
    // =========================================================

    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            return "SYSTEM";
        }

        return authentication.getName();
    }

    // =========================================================
    // STATUS PARSER
    // =========================================================

    private SalesOrderStatus parseStatus(
            String status) {

        if (status == null
                || status.isBlank()) {

            throw new RuntimeException(
                    "Order status cannot be empty"
            );
        }

        try {

            return SalesOrderStatus.valueOf(
                    status.trim().toUpperCase()
            );

        } catch (IllegalArgumentException ex) {

            throw new RuntimeException(
                    "Invalid order status: "
                            + status
            );
        }
    }

    // =========================================================
    // STATUS TRANSITION
    // =========================================================

    private boolean isValidStatusTransition(
            SalesOrderStatus current,
            SalesOrderStatus target) {

        if (current == null
                || target == null) {

            return false;
        }

        return switch (current) {

            case PENDING ->
                    target == SalesOrderStatus.CONFIRMED
                            || target == SalesOrderStatus.CANCELLED;

            case CONFIRMED ->
                    target == SalesOrderStatus.PROCESSING
                            || target == SalesOrderStatus.CANCELLED;

            case PROCESSING ->
                    target == SalesOrderStatus.SHIPPED;

            case SHIPPED ->
                    target == SalesOrderStatus.DELIVERED;

            case DELIVERED,
                 CANCELLED ->
                    false;
        };
    }

    // =========================================================
    // ORDER NUMBER
    // =========================================================

    private String generateOrderNumber() {

        long nextNumber =
                salesOrderRepository.count() + 1;

        return String.format(
                "SO-%05d",
                nextNumber
        );
    }

    // =========================================================
    // RESPONSE MAPPING
    // =========================================================

    private SalesOrderResponse mapToResponse(
            SalesOrder order) {

        List<SalesOrderItem> items =
                salesOrderItemRepository
                        .findBySalesOrderId(
                                order.getId()
                        );

        return mapToResponse(
                order,
                items
        );
    }

    private SalesOrderResponse mapToResponse(
            SalesOrder order,
            List<SalesOrderItem> items) {

        SalesOrderResponse response =
                new SalesOrderResponse();

        response.setId(
                order.getId()
        );

        response.setOrderNumber(
                order.getOrderNumber()
        );

        if (order.getCustomer() != null) {

            response.setCustomerId(
                    order.getCustomer().getId()
            );

            response.setCustomerName(
                    order.getCustomer().getName()
            );
        }

        response.setOrderDate(
                order.getOrderDate()
        );

        response.setTotalAmount(
                order.getTotalAmount()
        );

        /*
         * IMPORTANT:
         * SalesOrderResponse.status is SalesOrderStatus.
         * Do NOT use order.getStatus().name().
         */
        response.setStatus(
                order.getStatus()
        );

        response.setPaymentStatus(
                order.getPaymentStatus()
        );

        response.setShippingAddress(
                order.getShippingAddress()
        );

        response.setNotes(
                order.getNotes()
        );

        /*
         * createdBy is intentionally not mapped.
         *
         * SalesOrderResponse does not contain
         * createdBy field or setCreatedBy().
         */

        List<SalesOrderItemResponse> itemResponses =
                items.stream()
                        .map(
                                this::mapItemToResponse
                        )
                        .toList();

        response.setItems(
                itemResponses
        );

        return response;
    }

    // =========================================================
    // ITEM MAPPING
    // =========================================================

    private SalesOrderItemResponse mapItemToResponse(
            SalesOrderItem item) {

        SalesOrderItemResponse response =
                new SalesOrderItemResponse();

        response.setId(
                item.getId()
        );

        response.setProductId(
                item.getProductId()
        );

        response.setProductName(
                item.getProductName()
        );

        response.setQuantity(
                item.getQuantity()
        );

        response.setUnitPrice(
                item.getUnitPrice()
        );

        response.setSubtotal(
                item.getSubtotal()
        );

        return response;
    }
}