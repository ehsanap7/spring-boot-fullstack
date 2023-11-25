package com.ehsan.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.ehsan.model.customer.Customer;
import com.ehsan.model.enums.Gender;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {

        //Give
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Jack");
        when(resultSet.getString("email")).thenReturn("jack@gmail.com");
        when(resultSet.getInt("age")).thenReturn(20);
        when(resultSet.getString("gender")).thenReturn("MALE");

        //When
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        Customer result = customerRowMapper.mapRow(resultSet, 1);

        Customer expectedCustomer = new Customer(1,"Jack","jack@gmail.com", "password", 20,Gender.MALE);

        //Then
        assertThat(expectedCustomer).isEqualTo(result);

    }
}