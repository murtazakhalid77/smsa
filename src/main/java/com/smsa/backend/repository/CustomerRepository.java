package com.smsa.backend.repository;

import com.smsa.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    List<Customer> findAllByIsPresent(boolean b);
    Optional<Customer> findByAccountNumber(String accountNumber);
}
