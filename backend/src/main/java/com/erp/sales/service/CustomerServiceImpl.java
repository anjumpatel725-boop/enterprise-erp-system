package com.erp.sales.service;

import com.erp.audit.service.AuditLogService;
import com.erp.sales.dto.CustomerRequest;
import com.erp.sales.dto.CustomerResponse;
import com.erp.sales.entity.Customer;
import com.erp.sales.repository.CustomerRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AuditLogService auditLogService;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            AuditLogService auditLogService) {

        this.customerRepository = customerRepository;
        this.auditLogService = auditLogService;
    }

     @Override
      @Transactional(readOnly = true)
      public Page<CustomerResponse> getCustomersPaginated(
        int page,
        int size) {

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(
                    Sort.Direction.DESC,
                    "createdAt"
            )
    );

    return customerRepository
            .findAllByOrderByCreatedAtDesc(pageable)
            .map(this::mapToResponse);
}
    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {

        Customer customer = new Customer();

        customer.setCustomerCode(request.getCustomerCode());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setCountry(request.getCountry());
        customer.setStatus(request.getStatus());

        Customer savedCustomer = customerRepository.save(customer);

        // AUDIT LOG
        auditLogService.log(
                getCurrentUsername(),
                "CREATE",
                "SALES",
                "Customer",
                savedCustomer.getId(),
                "Customer created: " + savedCustomer.getName()
        );

        return mapToResponse(savedCustomer);
    }
     
    @Override
    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id: " + id
                        ));

        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id: " + id
                        ));

        customer.setCustomerCode(request.getCustomerCode());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setCountry(request.getCountry());
        customer.setStatus(request.getStatus());

        Customer updatedCustomer = customerRepository.save(customer);

        // AUDIT LOG
        auditLogService.log(
                getCurrentUsername(),
                "UPDATE",
                "SALES",
                "Customer",
                updatedCustomer.getId(),
                "Customer updated: " + updatedCustomer.getName()
        );

        return mapToResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id: " + id
                        ));

        String customerName = customer.getName();

        customerRepository.deleteById(id);

        // AUDIT LOG
        auditLogService.log(
                getCurrentUsername(),
                "DELETE",
                "SALES",
                "Customer",
                id,
                "Customer deleted: " + customerName
        );
    }

    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return "SYSTEM";
        }

        return authentication.getName();
    }

    private CustomerResponse mapToResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setCustomerCode(customer.getCustomerCode());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setCity(customer.getCity());
        response.setState(customer.getState());
        response.setCountry(customer.getCountry());
        response.setStatus(customer.getStatus());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());

        return response;
    }
}