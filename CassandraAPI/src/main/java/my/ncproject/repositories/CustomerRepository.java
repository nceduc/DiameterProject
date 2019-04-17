package my.ncproject.repositories;

import my.ncproject.domain.Customer;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.lang.annotation.Native;
import java.util.Optional;
import java.util.UUID;


public interface CustomerRepository extends CrudRepository<Customer, String> {
}
