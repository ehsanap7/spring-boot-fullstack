package com.ehsan;

import com.ehsan.model.customer.Customer;
import com.ehsan.repository.ICustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(Main.class, args);
    }

//    @Bean
//    public CommandLineRunner runner(ICustomerRepository iCustomerRepository) {
//        return args -> {
//            iCustomerRepository.save(new Customer("Ehsan","eap.it95@gmail.com",27));
//            iCustomerRepository.save(new Customer("Saeed","saeed.it95@gmail.com",27));
//            iCustomerRepository.save(new Customer("Hamid","hamid.it95@gmail.com",27));
//        };
//    }


}
