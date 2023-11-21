package com.ehsan.service;

import com.ehsan.exceptions.ConflictError;
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
    public Customer insertCustomer(Customer customer) {
        if (existsCustomerByEmail(customer.getEmail())) {
            throw new ConflictError("Email [%s] is repeated".formatted(customer.getEmail()));
        } else {
            return customerDAO.insertCustomer(customer);
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer old = getCustomer(customer.getId());
        if (!old.getEmail().equals(customer.getEmail()) && existsCustomerByEmail(customer.getEmail())) {
            throw new ConflictError("Email [%s] is repeated".formatted(customer.getEmail()));
        } else {
            return customerDAO.updateCustomer(customer);
        }
    }

    @Override
    public Boolean deleteCustomer(Integer customerId) {
        try {
            Customer customer = getCustomer(customerId);
            customerDAO.deleteCustomer(customer);
            return true;
        } catch (Exception e) {
            return false;
        }
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
