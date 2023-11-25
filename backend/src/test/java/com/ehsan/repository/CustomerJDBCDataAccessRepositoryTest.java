package com.ehsan.repository;

import com.ehsan.AbstractTestContainer;
import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
        Customer expectedCustomer = new Customer(
                faker.name().firstName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );
        underTest.insertCustomer(expectedCustomer);

        //when
        List<Customer> retrievedCustomers = underTest.getCustomers();

        //then
        assertThat(retrievedCustomers).isNotEmpty();
    }

    @Test
    void getCustomer() {
        //Give
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().firstName(),
                email,
                "password", 20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);
        int id = underTest.getCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
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

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //Give
        int id = -1;

        //When
        var actual = underTest.getCustomer(id);

        //Then
        assertThat(actual).isEmpty();
    }


    @Test
    void insertCustomer() {
        //Give
        Customer expectedCustomer = new Customer(
                faker.name().firstName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );

        //When
        Customer actualCustomers = underTest.insertCustomer(expectedCustomer);

        //Then
        assertThat(actualCustomers).isNotNull();
        assertThat(actualCustomers.getName()).isEqualTo(expectedCustomer.getName());
        assertThat(actualCustomers.getEmail()).isEqualTo(expectedCustomer.getEmail());
        assertThat(actualCustomers.getAge()).isEqualTo(expectedCustomer.getAge());
        assertThat(actualCustomers.getGender()).isEqualTo(expectedCustomer.getGender());

    }

    @Test
    void updateCustomer() {
        //Give
        Customer originalCustomer = new Customer(
                faker.name().firstName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );
        Customer originalCustomerId = underTest.insertCustomer(originalCustomer);

        Customer updatedCustomer = new Customer(
                originalCustomerId.getId(),
                "New Name",
                "new.email@example.com",
                "password", 25,
                Gender.FEMALE
        );


        //When
        Customer actualCustomer = underTest.updateCustomer(updatedCustomer);

        //Then
        assertThat(actualCustomer).isNotNull();
        assertThat(actualCustomer.getName()).isEqualTo(updatedCustomer.getName());
        assertThat(actualCustomer.getEmail()).isEqualTo(updatedCustomer.getEmail());
        assertThat(actualCustomer.getAge()).isEqualTo(updatedCustomer.getAge());
        assertThat(actualCustomer.getGender()).isEqualTo(updatedCustomer.getGender());

    }

    @Test
    void deleteCustomer() {
        //Give
        Customer customer = new Customer(
                faker.name().firstName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );
        Customer insertedCustomer = underTest.insertCustomer(customer);

        //When
        underTest.deleteCustomerById(insertedCustomer.getId());

        //Then
        Optional<Customer> deletedCustomer = underTest.getCustomer(insertedCustomer.getId());
        assertThat(deletedCustomer).isNotPresent();
    }

    @Test
    void existsCustomerByEmail() {
        //Give
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().firstName(),
                email,
                "password", 20,
                Gender.MALE
        );
        underTest.insertCustomer(customer);

        //When
        var actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmail_WithNonExistingEmail_ReturnsFalse() {
        //Give
        String nonExistingEmail = "nonexisting.email@example.com";

        //When
        var actual = underTest.existsCustomerByEmail(nonExistingEmail);

        //Then
        assertThat(actual).isFalse();

    }

    @Test
    void existsCustomerById() {
        //Give
        Customer customer = new Customer(
                faker.name().firstName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", 20,
                Gender.MALE
        );
        Customer insertedCustomer = underTest.insertCustomer(customer);

        //When
        var actual = underTest.existsCustomerById(insertedCustomer.getId());


        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerById_WithNonExistingId_ReturnsFalse() {
        //Given
        Integer nonExistingId = -1;

        //When
        var actual = underTest.existsCustomerById(nonExistingId);

        //Then
        assertThat(actual).isFalse();
    }

}