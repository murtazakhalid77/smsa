package com.smsa.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.CustomerDTO;
import com.smsa.backend.model.Customer;
import com.smsa.backend.service.CustomerService;
import com.smsa.backend.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import com.smsa.backend.dto.CustomerDto
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @GetMapping("/customer")
    ResponseEntity<List<CustomerDTO>> getAllCustomer(@RequestParam("search") String search, @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                                     @RequestParam(value = "sort", defaultValue = "accountNumber") String sort) throws JsonProcessingException {
        SearchCriteria searchCriteria = new ObjectMapper().readValue(search, SearchCriteria.class);
        Page<Customer> customers =customerService.getAllCustomer(searchCriteria, PageRequest.of(page, size,  Sort.by(sort).descending()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(customers.getTotalElements())); //
        return ResponseEntity.ok()
                .headers(headers)// Set the headers
                .body(customerService.toDto(customers));
    }
    @PostMapping("/customer")
    ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customerDTO){
        return  ResponseEntity.ok(customerService.addCustomer(customerDTO));
    }
    @GetMapping("/customer/{accountNumber}")
    ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String accountNumber){
        return  ResponseEntity.ok(customerService.getCustomerById(accountNumber));
    }

    @PatchMapping("/customer/{accountNumber}")
    ResponseEntity<CustomerDTO> updateCustomerByAccountNumber(@PathVariable String accountNumber, @RequestBody CustomerDTO customerDTO){
        return  ResponseEntity.ok(customerService.updateCustomerByAccountNumber(accountNumber, customerDTO));
    }
    @DeleteMapping("/customer/{id}")
    ResponseEntity<CustomerDTO> deleteCustomerById(@PathVariable Long id){
        return  ResponseEntity.ok(customerService.deleteCustomerById(id));
    }
}
