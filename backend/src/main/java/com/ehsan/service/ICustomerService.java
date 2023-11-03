package com.ehsan.service;

import com.ehsan.model.customer.Customer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICustomerService {

    List<Customer> getCustomers();

    Customer getCustomer(Integer id);

    void insertCustomer(Customer customer);
    boolean existsCustomerByEmail(@Param("email") String email);
    boolean existsCustomerById(Integer id);


}
