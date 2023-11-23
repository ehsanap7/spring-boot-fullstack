package com.ehsan.repository;

import com.ehsan.AbstractTestContainer;
import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ICustomerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private ICustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().firstName(),
                email,
                20,
                Gender.MALE
        );

        underTest.save(customer);

        underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        var actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerById() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().firstName(),
                email,
                20,
                Gender.MALE
        );

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        var actual = underTest.existsCustomerById(id);

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

    @Test
    void existsCustomerByEmail_WithNonExistingEmail_ReturnsFalse() {
        //Give
        String nonExistingEmail = "nonexisting.email@example.com";

        //When
        var actual = underTest.existsCustomerByEmail(nonExistingEmail);

        //Then
        assertThat(actual).isFalse();

    }

}