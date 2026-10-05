package com.erp.sales.service;

import com.erp.sales.dto.CustomerRequest;
import com.erp.sales.dto.CustomerResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse getCustomerById(Long id);

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    void deleteCustomer(Long id);
    Page<CustomerResponse> getCustomersPaginated(
        int page,
        int size
);
}