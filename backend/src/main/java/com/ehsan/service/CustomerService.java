package com.ehsan.service;

import com.ehsan.dto.CustomerDTO;
import com.ehsan.dto.CustomerRegistrationDto;
import com.ehsan.exceptions.ConflictError;
import com.ehsan.exceptions.DuplicateResourceException;
import com.ehsan.exceptions.RequestValidationException;
import com.ehsan.exceptions.ResourceNotFound;
import com.ehsan.mapper.CustomerDTOMapper;
import com.ehsan.model.customer.Customer;
import com.ehsan.repository.CustomerDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerDAO customerDAO;

    private final CustomerDTOMapper customerDTOMapper;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO, CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder) {
        this.customerDAO = customerDAO;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public List<CustomerDTO> getCustomers() {
        return customerDAO.getCustomers()
                .stream().map(customerDTOMapper).collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Integer id) {
        return customerDAO.getCustomer(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] Not Found".formatted(id)));
    }

    @Override
    public Customer insertCustomer(CustomerRegistrationDto customerRegistrationDto) {
        if (existsCustomerByEmail(customerRegistrationDto.email())) {
            throw new ConflictError("Email [%s] is repeated".formatted(customerRegistrationDto.email()));
        } else {
            Customer customer = new Customer(customerRegistrationDto.name()
                    ,customerRegistrationDto.email()
                    ,passwordEncoder.encode(customerRegistrationDto.password())
                    ,customerRegistrationDto.age()
                    ,customerRegistrationDto.gender());
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            return customerDAO.insertCustomer(customer);
        }
    }

    @Override
    public void updateCustomer(CustomerDTO updateRequest) {
        Customer customer = customerDAO.getCustomer(updateRequest.id())
                .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] Not Found".formatted(updateRequest.id())));
        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDAO.existsCustomerByEmail(updateRequest.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (updateRequest.gender() != null && !updateRequest.gender().equals(customer.getGender())) {
            customer.setGender(updateRequest.gender());
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
