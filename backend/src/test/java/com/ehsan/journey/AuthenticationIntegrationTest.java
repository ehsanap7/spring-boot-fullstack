package com.ehsan.journey;

import com.ehsan.dto.CustomerDTO;
import com.ehsan.dto.CustomerRegistrationDto;
import com.ehsan.dto.auth.AuthenticationRequest;
import com.ehsan.dto.auth.AuthenticationResponse;
import com.ehsan.jwt.JWTUtil;
import com.ehsan.model.enums.Gender;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    private static final Random random = new Random();
    private static final String AUTHENTICATION_PATH = "/api/v1/auth/login";
    private static final String CUSTOMER_PATH = "/api/v1/insert";

    @Test
    void canLogin() {
        //Give

        // Create registration customerRegistrationDto
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1, 100);
        String password = "password";
        CustomerRegistrationDto customerRegistrationDto = new CustomerRegistrationDto(name, email, password, age, Gender.MALE);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        // Send a post customerRegistrationDto
        webTestClient.post()
                .uri("/api/v1/insert")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(
                                customerRegistrationDto),
                        CustomerRegistrationDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

        String token = result.getResponseHeaders().get(AUTHORIZATION).get(0);

        CustomerDTO customerDTO = result.getResponseBody().customerDTO();

        assertThat(jwtUtil.isTokenValid(token, customerDTO.username())).isTrue();

        assertThat(customerDTO.username()).isEqualTo(email);
        assertThat(customerDTO.age()).isEqualTo(age);
        assertThat(customerDTO.name()).isEqualTo(name);
        assertThat(customerDTO.email()).isEqualTo(email);
        assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));

    }
}
