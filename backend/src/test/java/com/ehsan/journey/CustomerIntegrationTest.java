package com.ehsan.journey;

import com.ehsan.dto.CustomerDTO;
import com.ehsan.dto.CustomerRegistrationDto;
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
import static org.springframework.http.HttpHeaders.*;

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
        CustomerRegistrationDto request = new CustomerRegistrationDto(name, email, "password", age, Gender.MALE);

        // Send a post request
        String jwtToken = webTestClient.post()
                .uri("/api/v1/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .getFirst(AUTHORIZATION);

        // Get all customer
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri("api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();


        // Get customer by id
        assert allCustomers != null;
        int id = allCustomers.stream().filter(customer -> customer.email().equals(email)).map(CustomerDTO::id).findFirst().orElseThrow();

        // Make sure that customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(id, name, email, Gender.MALE, age, List.of("ROLE_USER"), email);

        assertThat(allCustomers)
                .contains(expectedCustomer);

        webTestClient.get()
                .uri("api/v1/customer" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .isEqualTo(expectedCustomer);

    }

    @Test
    void canDeleteCustomer() {
        // Create registration request for JWT
        Faker faker = new Faker();
        CustomerRegistrationDto requestJWT = new CustomerRegistrationDto(faker.name().fullName(), faker.internet().emailAddress(), "password", random.nextInt(1, 100), Gender.MALE);

        // Insert a Customer for JWT
        String jwtToken = webTestClient.post()
                .uri("api/v1/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestJWT), CustomerRegistrationDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .getFirst(AUTHORIZATION);

        // Create registration request for Delete
        faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1, 100);
        CustomerRegistrationDto request = new CustomerRegistrationDto(name, email, "password", age, Gender.MALE);

        // Insert a Customer for Delete
        webTestClient.post()
                .uri("api/v1/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customer
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri("api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // Get customer by id
        assert allCustomers != null;
        int id = allCustomers.stream().filter(customer -> customer.email().equals(email)).map(CustomerDTO::id).findFirst().orElseThrow();

        // Make sure that customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(id, name, email, Gender.MALE, age, List.of("ROLE_USER"), email);
        assertThat(allCustomers)
                .contains(expectedCustomer);

        // Delete the customer
        webTestClient.delete()
                .uri("api/v1/customer/delete" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

        // Not found test for deleted customer
        webTestClient.get()
                .uri("api/v1/customer" + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // Create registration request for JWT
        Faker faker = new Faker();
        CustomerRegistrationDto requestJWT = new CustomerRegistrationDto(faker.name().fullName(), faker.internet().emailAddress(), "password", random.nextInt(1, 100), Gender.MALE);

        // Insert a Customer for JWT
        String jwtToken = webTestClient.post()
                .uri("api/v1/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestJWT), CustomerRegistrationDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .getFirst(AUTHORIZATION);

        // Create registration request
        faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1, 100);
        CustomerRegistrationDto request = new CustomerRegistrationDto(name, email, "password", age, Gender.MALE);

        // Insert a Customer
        webTestClient.post()
                .uri("api/v1/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customer
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri("api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();


        // Get customer by id
        assert allCustomers != null;
        int id = allCustomers.stream().filter(customer -> customer.email().equals(email)).map(CustomerDTO::id).findFirst().orElseThrow();

        // Make sure that customer is present
        CustomerDTO insertedCustomer = new CustomerDTO(id, name, email, Gender.MALE, age, List.of("ROLE_USER"), email);
        assertThat(allCustomers).contains(insertedCustomer);

        // Create updated customer
        CustomerDTO actualCustomer = allCustomers.stream().filter(customer -> customer.email().equals(email)).findFirst().orElseThrow();
        faker = new Faker();
        Name updatedFakerName = faker.name();
        String updatedName = updatedFakerName.fullName();
        String updatedEmail = faker.internet().emailAddress();
        int updatedAge = random.nextInt(1, 100);
        CustomerDTO updatedRequest = new CustomerDTO(actualCustomer.id(), updatedName, updatedEmail, Gender.FEMALE, updatedAge, List.of("ROLE_USER"), updatedName);

        // Update the customer
        webTestClient.put()
                .uri("api/v1/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedRequest), CustomerDTO.class)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();


        // Get updated customer
        CustomerDTO result = webTestClient.get()
                .uri("api/v1/customer" + "/{id}", actualCustomer.id())
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // Test
        CustomerDTO expectedCustomer = new CustomerDTO(updatedRequest.id(), updatedName, updatedEmail, Gender.FEMALE, updatedAge, List.of("ROLE_USER"), updatedEmail);
        assertThat(result).isEqualTo(expectedCustomer);

    }

}
