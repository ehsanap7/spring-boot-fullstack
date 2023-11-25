package com.ehsan.service;

import com.ehsan.exceptions.ConflictError;
import com.ehsan.exceptions.DuplicateResourceException;
import com.ehsan.exceptions.RequestValidationException;
import com.ehsan.exceptions.ResourceNotFound;
import com.ehsan.model.customer.Customer;
import com.ehsan.repository.CustomerDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerDAO customerDAO;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO, PasswordEncoder passwordEncoder) {
        this.customerDAO = customerDAO;
        this.passwordEncoder = passwordEncoder;
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
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            return customerDAO.insertCustomer(customer);
        }
    }

    @Override
    public void updateCustomer(Customer updateRequest) {
        Customer customer = getCustomer(updateRequest.getId());
        boolean changes = false;

        if (updateRequest.getName() != null && !updateRequest.getName().equals(customer.getName())) {
            customer.setName(updateRequest.getName());
            changes = true;
        }

        if (updateRequest.getAge() != null && !updateRequest.getAge().equals(customer.getAge())) {
            customer.setAge(updateRequest.getAge());
            changes = true;
        }

        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(customer.getEmail())) {
            if (customerDAO.existsCustomerByEmail(updateRequest.getEmail())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(updateRequest.getEmail());
            changes = true;
        }

        if (updateRequest.getGender() != null && !updateRequest.getGender().equals(customer.getGender())) {
            customer.setGender(updateRequest.getGender());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No data changes found");
        }

        customerDAO.updateCustomer(customer);
    }

    public void deleteCustomerById(Integer customerId) {
        if (!customerDAO.existsCustomerById(customerId)) {
            throw new ResourceNotFound(
                    "Customer with id [%s] not found".formatted(customerId)
            );
        }

        customerDAO.deleteCustomerById(customerId);
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
