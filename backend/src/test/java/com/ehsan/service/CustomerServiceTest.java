package com.ehsan.service;

import com.ehsan.exceptions.ConflictError;
import com.ehsan.exceptions.DuplicateResourceException;
import com.ehsan.exceptions.RequestValidationException;
import com.ehsan.exceptions.ResourceNotFound;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
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
                25,
                Gender.MALE
        );

        //When
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(customer));

        //Then
        Customer actual = underTest.getCustomer(id);
        assertThat(actual).isEqualTo(customer);

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
                30,
                Gender.MALE
        );

        when(customerDAO.existsCustomerByEmail(customer.getEmail())).thenReturn(false);
        when(customerDAO.insertCustomer(customer)).thenReturn(customer);

        // When
        Customer insertedCustomer = underTest.insertCustomer(customer);

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
                30,
                Gender.MALE
        );

        // When
        when(customerDAO.existsCustomerByEmail(email)).thenReturn(true);
        assertThatThrownBy(() -> underTest.insertCustomer(customer))
                .isInstanceOf(ConflictError.class)
                .hasMessage("Email [%s] is repeated".formatted(email));

        // Then
        verify(customerDAO, never()).insertCustomer(customer);
    }

    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19, Gender.MALE);

        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";
        Customer updateRequest = new Customer(id, "Alexandro", newEmail, 23, Gender.FEMALE);

        when(customerDAO.existsCustomerByEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.getAge());
        assertThat(capturedCustomer.getGender()).isEqualTo(updateRequest.getGender());
    }


    @Test
    void canUpdateOnlyName() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19, Gender.MALE);

        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(customer));

        Customer updateRequest = new Customer(id, "Alexandro", customer.getEmail(), customer.getAge(), customer.getGender());

        // When
        underTest.updateCustomer(updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.getAge());
        assertThat(capturedCustomer.getGender()).isEqualTo(updateRequest.getGender());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id = 10;
        String originalEmail = "alex@gmail.com";
        int originalAge = 19;
        Gender originalGender = Gender.MALE;
        Customer originalCustomer = new Customer(id, "Alex", originalEmail, originalAge, originalGender);

        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        String updatedName = "Alexandro";
        Customer updateRequest = new Customer(id, updatedName, originalEmail, originalAge, originalGender);


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
        Customer originalCustomer = new Customer(id, originalName, originalEmail, originalAge, originalGender);

        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        int updatedAge = 25; // New age
        Customer updateRequest = new Customer(id, originalName, originalEmail, updatedAge, originalGender);

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
        Customer originalCustomer = new Customer(id, "Alex", "alex@gmail.com", 19, Gender.MALE);
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        String newEmail = "newemail@example.com";
        Customer updateRequest = new Customer(id, "Alex", newEmail, 19, Gender.MALE);
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
        Customer originalCustomer = new Customer(id, "Alex", "alex@gmail.com", 19, Gender.MALE);
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        String newEmail = "takenemail@example.com";
        Customer updateRequest = new Customer(id, "Alex", newEmail, 19, Gender.MALE);
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
        Customer originalCustomer = new Customer(id, "Alex", "alex@gmail.com", 19, Gender.MALE);
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        Gender newGender = Gender.FEMALE; // Different gender
        Customer updateRequest = new Customer(id, "Alex", "alex@gmail.com", 19, newGender);

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
        Customer originalCustomer = new Customer(id, "Alex", "alex@gmail.com", 19, Gender.MALE);
        when(customerDAO.getCustomer(id)).thenReturn(Optional.of(originalCustomer));

        Customer updateRequest = new Customer(id, "Alex", "alex@gmail.com", 19, Gender.MALE);

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