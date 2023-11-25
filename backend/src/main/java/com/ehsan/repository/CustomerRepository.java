package com.ehsan.repository;

import com.ehsan.model.customer.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
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
    public Customer insertCustomer(Customer customer) {
        return iCustomerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return iCustomerRepository.save(customer);
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        iCustomerRepository.deleteById(customerId);
    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        return iCustomerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return iCustomerRepository.existsCustomerById(id);
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        return iCustomerRepository.getCustomersByEmail(email);
    }
}
