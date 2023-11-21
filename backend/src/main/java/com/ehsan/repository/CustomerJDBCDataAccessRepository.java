package com.ehsan.repository;

import com.ehsan.model.customer.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("two")
public class CustomerJDBCDataAccessRepository implements CustomerDAO {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessRepository(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> getCustomers() {
        var sql = """
                select id, name, email, age, gender
                from customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomer(Integer id) {
        var sql = """
                select id, name, email, age, gender
                from customer
                where id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public Customer insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer (name, email, age, gender)
                values (?,?,?,?)
                """;
        int result = jdbcTemplate.update(sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender().name()
        );
        return getCustomer(result).orElseThrow();
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return null;
    }

    @Override
    public void deleteCustomer(Customer customer) {

    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        return true;
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return true;
    }



}