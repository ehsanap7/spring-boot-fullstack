package com.ehsan.repository;

import com.ehsan.AbstractTestContainer;
import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ICustomerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private ICustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {

        String emial = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().firstName(),
                emial,
                20,
                Gender.MALE
        );

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(emial))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        boolean actual = underTest.existsCustomerByEmail(emial);

        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerById() {

        String emial = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().firstName(),
                emial,
                20,
                Gender.MALE
        );

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(emial))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        boolean actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isTrue();

    }
}