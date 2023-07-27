package com.smsa.backend.controller;

import com.smsa.backend.dto.CustomerDTO;
import com.smsa.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
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
    ResponseEntity<List<CustomerDTO>> getAllCustomer(){
    return ResponseEntity.ok(customerService.getAllCustomer());
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
    ResponseEntity<CustomerDTO> updateCustomerById(@PathVariable String accountNumber, @RequestBody CustomerDTO customerDTO){
        return  ResponseEntity.ok(customerService.updateCustomerById(accountNumber, customerDTO));
    }
    @DeleteMapping("/customer/{id}")
    ResponseEntity<CustomerDTO> deleteCustomerById(@PathVariable Long id){
        return  ResponseEntity.ok(customerService.deleteCustomerById(id));
    }
}
