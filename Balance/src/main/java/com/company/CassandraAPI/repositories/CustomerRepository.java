package com.company.CassandraAPI.repositories;

import com.company.CassandraAPI.domain.Customer;
import org.springframework.data.repository.CrudRepository;


public interface CustomerRepository extends CrudRepository<Customer, String> {

}
