package com.ehsan.service;

import com.ehsan.exceptions.ResourceNotFound;
import com.ehsan.model.customer.Customer;
import com.ehsan.repository.CustomerDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("one") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }


    public List<Customer> getCustomers() {
        return customerDAO.getCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDAO.getCustomer(id)
                .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] Not Found".formatted(id)));
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerDAO.insertCustomer(customer);
    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        return customerDAO.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return customerDAO.existsCustomerById(id);
    }

}
