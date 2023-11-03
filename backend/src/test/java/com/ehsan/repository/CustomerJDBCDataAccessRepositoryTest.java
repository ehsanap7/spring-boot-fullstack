package com.ehsan.repository;

import com.ehsan.AbstractTestContainer;
import com.ehsan.model.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessRepositoryTest extends AbstractTestContainer {

    private CustomerJDBCDataAccessRepository underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessRepository(
                getJDBCTemplate(),
                customerRowMapper
        );
    }

    @Test
    void getCustomers() {
        //given
        Customer customer = new Customer(
                faker.name().firstName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        //when
        List<Customer> expectedCustomers = underTest.getCustomers();

        //then
        assertThat(expectedCustomers).isNotEmpty();
    }

    @Test
    void getCustomer() {
        //Give
        String emial = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().firstName(),
                emial,
                20
        );
        underTest.insertCustomer(customer);
        int id = underTest.getCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(emial))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        Optional<Customer> actual = underTest.getCustomer(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}