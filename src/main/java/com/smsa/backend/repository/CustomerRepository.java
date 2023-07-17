package com.smsa.backend.repository;

import com.smsa.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findAllByIsActive(boolean b);
}
