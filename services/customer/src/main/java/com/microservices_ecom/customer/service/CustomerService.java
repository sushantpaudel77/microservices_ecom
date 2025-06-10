package com.microservices_ecom.customer.service;

import com.microservices_ecom.customer.customer.Customer;
import com.microservices_ecom.customer.customer.CustomerRequest;
import com.microservices_ecom.customer.customer.CustomerResponse;
import com.microservices_ecom.customer.exception.CustomerNotFoundException;
import com.microservices_ecom.customer.repository.CustomerRepository;
import com.microservices_ecom.customer.utils.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public String createRequest(CustomerRequest request) {
        var customer = customerRepository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    @Transactional
    public void updateCustomer(CustomerRequest request) {
        var customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot update customer: No Customer found with provided ID: %s",
                                request.id())));
        mergeCustomer(customer, request);
        customerRepository.save(customer);
    }

    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .toList();
    }

    public CustomerResponse findById(String customerId) {
        return customerRepository.findById(customerId)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Customer not found with the ID: %s", customerId)));
    }

    public Boolean existsById(String customerId) {
        return customerRepository.findById(customerId)
                .isPresent();
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.hasText(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if (StringUtils.hasText(request.lastname())) {
            customer.setLastname(request.lastname());
        }
        if (StringUtils.hasText(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }
}