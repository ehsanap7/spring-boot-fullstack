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
        final var sql = """
                INSERT INTO customer (name, email, age, gender)
                VALUES (?, ?, ?, ?)
                RETURNING id
                """;

        Integer id = jdbcTemplate.queryForObject(sql, new Object[]{
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender().name()
        }, Integer.class);
        return getCustomer(id).get();
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        var sql = """
                UPDATE customer
                SET name = ?, email = ?, age = ?, gender = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender().name(),
                customer.getId());
        return getCustomer(customer.getId()).get();
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        var sql = """
                DELETE FROM customer
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, customerId);
    }

    public boolean existsCustomerByEmail(String email) {
        var sql = """
                select count(*)
                from customer
                where email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        var sql = """
                select count(*)
                from customer
                where id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

}