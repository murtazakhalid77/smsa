package com.smsa.backend.controller;

import com.smsa.backend.dto.CustomerDTO;
import com.smsa.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import com.smsa.backend.dto.CustomerDto
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/customer")
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
    @GetMapping("/customer/{id}")
    ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id){
        return  ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PatchMapping("/customer/{id}")
    ResponseEntity<CustomerDTO> updateCustomerById(@RequestBody CustomerDTO customerDTO){
        return  ResponseEntity.ok(customerService.updateCustomerById(customerDTO));
    }
    @PutMapping("/customer/{id}")
    ResponseEntity<CustomerDTO> deleteCustomerById(@PathVariable Long id){
        return  ResponseEntity.ok(customerService.deleteCustomerById(id));
    }
}
