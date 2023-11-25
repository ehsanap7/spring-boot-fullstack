package com.ehsan.repository;

import com.ehsan.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsCustomerByEmail(@Param("email") String email);
    boolean existsCustomerById(Integer id);
    Optional<Customer> getCustomersByEmail(String email);

}
