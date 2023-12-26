package com.ehsan.service;

import com.ehsan.dto.CustomerDTO;
import com.ehsan.dto.CustomerRegistrationDto;
import com.ehsan.model.customer.Customer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICustomerService {

    List<CustomerDTO> getCustomers();
    CustomerDTO getCustomer(Integer id);
    Customer insertCustomer(CustomerRegistrationDto customer);
    void updateCustomer(CustomerDTO updateRequest);
    void deleteCustomerById(Integer id);
    boolean existsCustomerByEmail(@Param("email") String email);
    boolean existsCustomerById(Integer id);


}
