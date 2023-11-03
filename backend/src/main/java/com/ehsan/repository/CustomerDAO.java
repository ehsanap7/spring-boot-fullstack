package com.ehsan.repository;

import com.ehsan.model.customer.Customer;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    public List<Customer> getCustomers();
    public Optional<Customer> getCustomer(Integer id);
    void insertCustomer(Customer customer);
    boolean existsCustomerByEmail(@Param("email") String email);
    boolean existsCustomerById(Integer id);

}

