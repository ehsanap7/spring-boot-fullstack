package com.ehsan.service;

import com.ehsan.dto.CustomerDTO;
import com.ehsan.dto.CustomerRegistrationDto;
import com.ehsan.exceptions.ConflictError;
import com.ehsan.exceptions.DuplicateResourceException;
import com.ehsan.exceptions.RequestValidationException;
import com.ehsan.exceptions.ResourceNotFound;
import com.ehsan.mapper.CustomerDTOMapper;
import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import com.ehsan.repository.CustomerDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();

    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO, customerDTOMapper, passwordEncoder);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCustomers() {
        //When
        underTest.getCustomers();
        //Then
        verify(customerDAO).getCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        int id = 1;
        Customer customer = new Customer(
                id,
                "Ehsan",
                "eap.it95@gmail.com",
                "password", 25,
                Gender.MALE
        );

        CustomerDTO expected = customerDTOMapper.apply(customer);

        //When
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(customer));

        //Then
        CustomerDTO actual = underTest.getCustomer(id);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void willGetCustomerThrowWhenEmpty() {
        //Given
        int id = 10;

        //When
        when(customerDAO.getCustomer(id)).thenReturn(Optional.empty());

        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Customer with id [%s] Not Found".formatted(id));

    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                "John",
                "john@example.com",
                "password", 30,
                Gender.MALE
        );

        String hashPassword = "!@$FYGHJHGFCGHVJB^%$#@BNHJ";

        when(passwordEncoder.encode(customer.getPassword())).thenReturn(hashPassword);
        when(customerDAO.existsCustomerByEmail(customer.getEmail())).thenReturn(false);
        when(customerDAO.insertCustomer(customer)).thenReturn(customer);

        // When
        CustomerRegistrationDto customerRegistrationDto = new CustomerRegistrationDto(customer.getName(), customer.getEmail(), customer.getPassword(), customer.getAge(), customer.getGender());
        Customer insertedCustomer = underTest.insertCustomer(customerRegistrationDto);

        // Then
        assertThat(insertedCustomer).isEqualTo(customer);
        verify(customerDAO).insertCustomer(customer);

    }

    @Test
    void wiltThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email = "john@example.com";
        Customer customer = new Customer(
                "John",
                email,
                "password", 30,
                Gender.MALE
        );
        CustomerRegistrationDto customerRegistrationDto = new CustomerRegistrationDto(
                "John",
                email,
                "password", 30,
                Gender.MALE
        );

        // When
        when(customerDAO.existsCustomerByEmail(email)).thenReturn(true);
        assertThatThrownBy(() -> underTest.insertCustomer(customerRegistrationDto))
                .isInstanceOf(ConflictError.class)
                .hasMessage("Email [%s] is repeated".formatted(email));

        // Then
        verify(customerDAO, never()).insertCustomer(customer);
    }

    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", "password", 19, Gender.MALE);

        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";
        CustomerDTO updateRequest = new CustomerDTO(id, "Alexandro", newEmail, Gender.FEMALE, 23, List.of("ROLE_USER"), newEmail);

        when(customerDAO.existsCustomerByEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(updateRequest.gender());
    }


    @Test
    void canUpdateOnlyName() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", "password", 19, Gender.MALE);

        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(customer));

        CustomerDTO updateRequest = new CustomerDTO(id, "Alexandro", customer.getEmail(), customer.getGender(), customer.getAge(), List.of("ROLE_USER"), customer.getEmail());

        // When
        underTest.updateCustomer(updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(updateRequest.gender());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id = 10;
        String originalEmail = "alex@gmail.com";
        int originalAge = 19;
        Gender originalGender = Gender.MALE;
        Customer originalCustomer = new Customer(id, "Alex", originalEmail, "password", originalAge, originalGender);

        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        String updatedName = "Alexandro";
        CustomerDTO updateRequest = new CustomerDTO(id, updatedName, originalEmail, originalGender, originalAge, List.of("ROLE_USER"), originalEmail);


        // When
        underTest.updateCustomer(updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updatedName);
        assertThat(capturedCustomer.getEmail()).isEqualTo(originalEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(originalAge);
        assertThat(capturedCustomer.getGender()).isEqualTo(originalGender);
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 10;
        String originalName = "Alex";
        String originalEmail = "alex@gmail.com";
        int originalAge = 19;
        Gender originalGender = Gender.MALE;
        Customer originalCustomer = new Customer(id, originalName, originalEmail, "password", originalAge, originalGender);

        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        int updatedAge = 25; // New age
        CustomerDTO updateRequest = new CustomerDTO(id, originalName, originalEmail, originalGender, updatedAge, List.of("ROLE_USER"), originalEmail);

        // When
        underTest.updateCustomer(updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(originalName);
        assertThat(capturedCustomer.getEmail()).isEqualTo(originalEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(updatedAge);
        assertThat(capturedCustomer.getGender()).isEqualTo(originalGender);
    }

    @Test
    void updateCustomerEmail_WhenEmailIsNotTaken_UpdatesSuccessfully() {
        // Given
        int id = 10;
        Customer originalCustomer = new Customer(id, "Alex", "alex@gmail.com", "password", 19, Gender.MALE);
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        String newEmail = "newemail@example.com";
        CustomerDTO updateRequest = new CustomerDTO(id, "Alex", newEmail, Gender.MALE, 19, List.of("ROLE_USER"), newEmail);
        when(customerDAO.existsCustomerByEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void updateCustomerEmail_WhenEmailIsTaken_ThrowsDuplicateResourceException() {
        // Given
        int id = 10;
        Customer originalCustomer = new Customer(id, "Alex", "alex@gmail.com", "password", 19, Gender.MALE);
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        String newEmail = "takenemail@example.com";
        CustomerDTO updateRequest = new CustomerDTO(id, "Alex", newEmail, Gender.MALE, 19, List.of("ROLE_USER"), newEmail);
        when(customerDAO.existsCustomerByEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        //Then
        verify(customerDAO, never()).updateCustomer(any());

    }

    @Test
    void canUpdateOnlyCustomerGender() {
        // Given
        int id = 10;
        Customer originalCustomer = new Customer(id, "Alex", "alex@gmail.com", "password", 19, Gender.MALE);
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        Gender newGender = Gender.FEMALE; // Different gender
        CustomerDTO updateRequest = new CustomerDTO(id, "Alex", "alex@gmail.com", newGender,19, List.of("ROLE_USER"), "alex@gmail.com");

        // When
        underTest.updateCustomer(updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getGender()).isEqualTo(newGender);
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        int id = 10;
        Customer originalCustomer = new Customer(id, "Alex", "alex@gmail.com", "password", 19, Gender.MALE);
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        CustomerDTO updateRequest = new CustomerDTO(id, "Alex", "alex@gmail.com",  Gender.MALE,19, List.of("ROLE_USER"), "alex@gmail.com");

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");

        //Then
        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void deleteCustomer() {
        // Given
        int customerId = 10;
        when(customerDAO.existsCustomerById(customerId)).thenReturn(true);

        // When
        underTest.deleteCustomerById(customerId);

        // Then
        verify(customerDAO).deleteCustomerById(customerId);
    }

    @Test
    void willThrowWhenDeleteCustomerByIdNotExists() {
        // Given
        int id = 10;
        when(customerDAO.existsCustomerById(id)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));

        // Then
        verify(customerDAO, never()).deleteCustomerById(id);
    }


}