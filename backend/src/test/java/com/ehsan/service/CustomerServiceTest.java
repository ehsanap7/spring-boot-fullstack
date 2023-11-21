package com.ehsan.service;

import com.ehsan.exceptions.ResourceNotFound;
import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import com.ehsan.repository.CustomerDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCustomers() {

        underTest.getCustomers();

        verify(customerDAO).getCustomers();

    }

    @Test
    void canGetCustomer() {

        int id = 1;

        Customer customer = new Customer(
                id,
                "Ehsan",
                "eap.it95@gmail.com",
                25,
                Gender.MALE
        );

        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void willGetCustomerThrowWhenEmpty() {

        int id = 10;

        when(customerDAO.getCustomer(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Customer with id [%s] Not Found".formatted(id));

    }

}