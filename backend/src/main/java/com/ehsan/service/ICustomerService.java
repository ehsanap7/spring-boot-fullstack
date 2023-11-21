package com.ehsan.service;

import com.ehsan.model.customer.Customer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICustomerService {

    List<Customer> getCustomers();

    Customer getCustomer(Integer id);

    Customer insertCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Boolean deleteCustomer(Integer id);
    boolean existsCustomerByEmail(@Param("email") String email);
    boolean existsCustomerById(Integer id);


}
