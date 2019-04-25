package com.company.CassandraAPI.services;

import com.company.CassandraAPI.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> listAll();

    Optional<Customer> getByNumber(String number);

    Customer create(Customer customer);

    void delete(String number);

}
