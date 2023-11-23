package com.ehsan.service;

import com.ehsan.model.customer.Customer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICustomerService {

    List<Customer> getCustomers();

    Customer getCustomer(Integer id);

    Customer insertCustomer(Customer customer);
    void updateCustomer(Customer updateRequest);
    void deleteCustomerById(Integer id);
    boolean existsCustomerByEmail(@Param("email") String email);
    boolean existsCustomerById(Integer id);


}
