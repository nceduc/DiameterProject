package com.company.cassandraAPI.services;

import com.company.cassandraAPI.domain.Customer;
import com.company.cassandraAPI.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public List<Customer> listAll() {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        return customers;
    }

    @Override
    public Optional<Customer> getByNumber(String number) {
        return customerRepository.findById(number);
    }


    @Override
    public Customer create(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    @Override
    public void delete(String number) {
        customerRepository.deleteById(number);
    }

}
