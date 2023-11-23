package com.ehsan.controller;

import com.ehsan.exceptions.ConflictError;
import com.ehsan.model.customer.Customer;
import com.ehsan.service.ICustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("api/v1/insert")
    public void insertCustomer(@RequestBody Customer customer) { iCustomerService.insertCustomer(customer); }

    @PutMapping("api/v1/update")
    public void updateCustomer(@RequestBody Customer customer) {
        iCustomerService.updateCustomer(customer);
    }

    @DeleteMapping("api/v1/customer/delete/{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        iCustomerService.deleteCustomerById(id);
    }

}
