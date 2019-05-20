package com.company.balance;

import com.company.cassandraAPI.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

class GetBalance implements CrudRepository<Customer, String> {
    @Override
    public <S extends Customer> S save(S s) {
        return null;
    }

    @Override
    public <S extends Customer> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Customer> findById(String clientID) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<Customer> findAll() {
        return null;
    }

    @Override
    public Iterable<Customer> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Customer customer) {

    }

    @Override
    public void deleteAll(Iterable<? extends Customer> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
