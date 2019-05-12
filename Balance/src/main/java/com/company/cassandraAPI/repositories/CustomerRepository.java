package com.company.cassandraAPI.repositories;

import com.company.cassandraAPI.domain.Customer;
import org.springframework.data.repository.CrudRepository;


public interface CustomerRepository extends CrudRepository<Customer, String> {

}
