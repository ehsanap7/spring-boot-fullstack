package com.ehsan.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerRepositoryTest {

    AutoCloseable autoCloseable = null;
    private CustomerRepository underTest;
    @Mock
    private ICustomerRepository iCustomerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerRepository(iCustomerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getCustomers() {
        underTest.getCustomers();
        verify(iCustomerRepository).findAll();
    }

    @Test
    void getCustomer() {

        int id = 0;

        underTest.getCustomer(id);

        verify(iCustomerRepository).findById(id);

    }

    @Test
    void insertCustomer() {
    }

    @Test
    void existsCustomerByEmail() {
    }

    @Test
    void existsCustomerById() {
    }
}