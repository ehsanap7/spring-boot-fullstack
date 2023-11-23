package com.ehsan.journey;

import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random random = new Random();

    @Test
    void canRegisterCustomer() {
        // Create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1, 100);
        Customer request = new Customer(name, email, age, Gender.MALE);

        // Send a post request
        webTestClient.post()
                .uri("api/v1/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), Customer.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri("api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        // Make sure that customer is present
        Customer expectedCustomer = new Customer(name, email, age, Gender.MALE);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        // Get customer by id
        int id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);

        webTestClient.get()
                .uri("api/v1/customer" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);

    }

    @Test
    void canDeleteCustomer() {
        // Create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1, 100);
        Customer request = new Customer(name, email, age, Gender.MALE);

        // Insert a Customer
        webTestClient.post()
                .uri("api/v1/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), Customer.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri("api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        // Make sure that customer is present
        Customer expectedCustomer = new Customer(name, email, age, Gender.MALE);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        // Get customer by id
        int id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);


        // Delete the customer
        webTestClient.delete()
                .uri("api/v1/customer/delete" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // Not found test for deleted customer
        webTestClient.get()
                .uri("api/v1/customer" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void canUpdateCustomer() {
        // Create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1, 100);
        Customer request = new Customer(name, email, age, Gender.MALE);

        // Insert a Customer
        webTestClient.post()
                .uri("api/v1/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), Customer.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri("api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        // Make sure that customer is present
        Customer insertedCustomer = new Customer(name, email, age, Gender.MALE);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(insertedCustomer);


        // Create updated customer
        Customer actualCustomer = allCustomers.stream().filter(customer -> customer.getEmail().equals(email)).findFirst().orElseThrow();
        faker = new Faker();
        Name updatedFakerName = faker.name();
        String updatedName = updatedFakerName.fullName();
        String updatedEmail = faker.internet().emailAddress();
        int updatedAge = random.nextInt(1, 100);
        Customer updatedRequest = new Customer(actualCustomer.getId(), updatedName, updatedEmail, updatedAge, Gender.FEMALE);

        // Update the customer
        webTestClient.put()
                .uri("api/v1/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedRequest), Customer.class)
                .exchange()
                .expectStatus()
                .isOk();


        // Get updated customer
        Customer result = webTestClient.get()
                .uri("api/v1/customer" + "/{id}", actualCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // Test
        Customer expectedCustomer = new Customer(updatedRequest.getId(), updatedName, updatedEmail, updatedAge, Gender.FEMALE);
        assertThat(result).isEqualTo(expectedCustomer);

    }

}
