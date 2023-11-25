package com.ehsan.repository;

import com.ehsan.model.customer.Customer;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    public List<Customer> getCustomers();
    public Optional<Customer> getCustomer(Integer id);
    Customer insertCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    void deleteCustomerById(Integer customerId);
    boolean existsCustomerByEmail(@Param("email") String email);
    boolean existsCustomerById(Integer id);
    Optional<Customer> selectUserByEmail(String email);

}

