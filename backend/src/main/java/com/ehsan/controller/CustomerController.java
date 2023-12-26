package com.ehsan.controller;

import com.ehsan.dto.CustomerDTO;
import com.ehsan.dto.CustomerRegistrationDto;
import com.ehsan.jwt.JWTUtil;
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
    public List<CustomerDTO> getCustomers() {
        return iCustomerService.getCustomers();
    }

    @GetMapping("api/v1/customer/{id}")
    public CustomerDTO getCustomer(@PathVariable("id") Integer id) {
        return iCustomerService.getCustomer(id);
    }

    @PostMapping("api/v1/insert")
    public ResponseEntity<?> insertCustomer(@RequestBody CustomerRegistrationDto customer) {
        iCustomerService.insertCustomer(customer);
        String jwtToken = jwtUtil.issueToken(customer.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken).build();
    }

    @PutMapping("api/v1/update")
    public void updateCustomer(@RequestBody CustomerDTO customerDTO) {
        iCustomerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("api/v1/customer/delete/{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        iCustomerService.deleteCustomerById(id);
    }

}
