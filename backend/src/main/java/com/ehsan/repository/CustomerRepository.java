package com.ehsan.repository;

import com.ehsan.model.customer.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("one")
public class CustomerRepository implements CustomerDAO {

    private final ICustomerRepository iCustomerRepository;

    public CustomerRepository(ICustomerRepository iCustomerRepository) {
        this.iCustomerRepository = iCustomerRepository;
    }

    @Override
    public List<Customer> getCustomers() {
        return iCustomerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomer(Integer id) {
        return iCustomerRepository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {

    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        return iCustomerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return iCustomerRepository.existsCustomerById(id);
    }
}
