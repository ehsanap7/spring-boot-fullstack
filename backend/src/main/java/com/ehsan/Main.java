package com.ehsan;

import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import com.ehsan.repository.ICustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@SpringBootApplication
public class Main {

    private static final Random random = new Random();

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner runner(ICustomerRepository iCustomerRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {
            Faker faker = new Faker();
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.MALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.FEMALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.FEMALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.MALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.MALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.FEMALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.FEMALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.MALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.MALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.FEMALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.FEMALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(),faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5,80), Gender.MALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(), faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5, 80), Gender.MALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(), faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5, 80), Gender.FEMALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(), faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5, 80), Gender.FEMALE));
            iCustomerRepository.save(new Customer(faker.name().fullName(), faker.internet().emailAddress(), passwordEncoder.encode(faker.phoneNumber().cellPhone()), random.nextInt(5, 80), Gender.MALE));

        };
    }


}
