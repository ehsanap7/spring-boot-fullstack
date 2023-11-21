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
    public ResponseEntity<?> insertCustomer(@RequestBody Customer customer){
        try {
            return ResponseEntity.ok(iCustomerService.insertCustomer(customer));
        }catch (ConflictError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("api/v1/update")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        try {
            return ResponseEntity.ok(iCustomerService.updateCustomer(customer));
        }catch (ConflictError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("api/v1/customer/delete/{id}")
    public Boolean deleteCustomer(@PathVariable("id") Integer id) {
        return iCustomerService.deleteCustomer(id);
    }

}
