package com.ehsan.controller;

import com.ehsan.model.customer.Customer;
import com.ehsan.service.ICustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final ICustomerService iCustomerService;

    public CustomerController(ICustomerService iCustomerService) {
        this.iCustomerService = iCustomerService;
    }

    @GetMapping("api/v1/customer")
    public List<Customer> getCustomers() {
        return iCustomerService.getCustomers();
    }

    @GetMapping("api/v1/customer/{id}")
    public Customer getCustomer(@PathVariable("id") Integer id) {
        return iCustomerService.getCustomer(id);
    }

    @PostMapping("api/v1/customer")
    public List<Customer> insertCustomer() {
        return iCustomerService.getCustomers();
    }

    @PutMapping("api/v1/customer")
    public List<Customer> updateCustomer() {
        return iCustomerService.getCustomers();
    }

    @DeleteMapping("api/v1/customer")
    public List<Customer> deleteCustomer() {
        return iCustomerService.getCustomers();
    }

}
