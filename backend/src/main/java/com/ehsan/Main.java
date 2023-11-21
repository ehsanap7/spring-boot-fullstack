package com.ehsan;

import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import com.ehsan.repository.ICustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner runner(ICustomerRepository iCustomerRepository) {
        return args -> {
            iCustomerRepository.save(new Customer("Ehsan3030","eap.it95@gmail.com",27, Gender.MALE));
            iCustomerRepository.save(new Customer("Zahraaaa2020","zahra95@gmail.com",27, Gender.FEMALE));
            iCustomerRepository.save(new Customer("AliEhsan","eap.it95555@gmail.com",27, Gender.MALE));
            iCustomerRepository.save(new Customer("Melisa","eap.it95@gmail.com",27, Gender.FEMALE));
            iCustomerRepository.save(new Customer("Sydney","eap.it95@gmail.com",27, Gender.FEMALE));
            iCustomerRepository.save(new Customer("Zahra","eap.it95@gmail.com",27, Gender.FEMALE));
            iCustomerRepository.save(new Customer("Jack","eap.it95@gmail.com",27, Gender.MALE));
            iCustomerRepository.save(new Customer("Pop","eap.it95@gmail.com",27, Gender.MALE));
            iCustomerRepository.save(new Customer("Xi","eap.it95@gmail.com",27, Gender.MALE));
        };
    }


}
