package com.ehsan.repository;

import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

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
        //When
        underTest.getCustomers();
        //Then
        verify(iCustomerRepository).findAll();
    }

    @Test
    void getCustomer() {
        //Given
        int id = 0;

        //When
        underTest.getCustomer(id);

        //Then
        verify(iCustomerRepository).findById(id);

    }

    @Test
    void insertCustomer() {
        //Given
        Customer originalCustomer = new Customer(
                "Jack",
                "nonexisting.email@example.com",
                20,
                Gender.MALE
        );

        //When
        underTest.insertCustomer(originalCustomer);

        //Then
        verify(iCustomerRepository).save(originalCustomer);
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = "nonexisting.email@example.com";

        //When
        underTest.existsCustomerByEmail(email);

        //Then
        verify(iCustomerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsCustomerById() {
        //Given
        int id = -1;

        //When
        underTest.getCustomer(id);

        //Then
        verify(iCustomerRepository).findById(id);
    }
}