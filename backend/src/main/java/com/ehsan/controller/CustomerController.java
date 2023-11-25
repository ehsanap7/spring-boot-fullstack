package com.ehsan.controller;

import com.ehsan.jwt.JWTUtil;
import com.ehsan.model.customer.Customer;
import com.ehsan.service.ICustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final ICustomerService iCustomerService;

    private final JWTUtil jwtUtil;

    public CustomerController(ICustomerService iCustomerService, JWTUtil jwtUtil) {
        this.iCustomerService = iCustomerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("api/v1/customer")
    public List<Customer> getCustomers() {
        return iCustomerService.getCustomers();
    }

    @GetMapping("api/v1/customer/{id}")
    public Customer getCustomer(@PathVariable("id") Integer id) {
        return iCustomerService.getCustomer(id);
    }

    @PostMapping("api/v1/insert")
    public ResponseEntity<?> insertCustomer(@RequestBody Customer customer) {
        iCustomerService.insertCustomer(customer);
        String jwtToken = jwtUtil.issueToken(customer.getEmail(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken).build();
    }

    @PutMapping("api/v1/update")
    public void updateCustomer(@RequestBody Customer customer) {
        iCustomerService.updateCustomer(customer);
    }

    @DeleteMapping("api/v1/customer/delete/{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        iCustomerService.deleteCustomerById(id);
    }

}
