package com.microservices_ecom.customer.repository;

import com.microservices_ecom.customer.customer.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
