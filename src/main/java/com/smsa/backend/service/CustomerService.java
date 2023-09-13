package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordAlreadyExistException;
import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.dto.CustomerDTO;
import com.smsa.backend.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.smsa.backend.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Page<Customer> getAllCustomer(Pageable pageable) {
        Page<Customer> customer =this.customerRepository.findAll(pageable);
        if(!customer.isEmpty()){
            return customer;
        }
        return null;

    }

    public List<Customer> getAllCustomer() {
        return this.customerRepository.findAll();
    }

    public CustomerDTO addCustomer(CustomerDTO customerDTO) {

        Optional<Customer> duplicateCustomer = customerRepository.findByAccountNumber(customerDTO.getAccountNumber());
        if(duplicateCustomer.isPresent()){
            throw new RecordAlreadyExistException("Cannot create customer with duplicate account number: ");
        }else{
            Customer customer = toDomain(customerDTO);
            return toDto(customerRepository.save(toDomain(customerDTO)));
        }
    }

   public List<CustomerDTO> toDto(Page<Customer> customer){
      List<CustomerDTO> customerDTOS = customer.getContent().stream().map(c->toDto((Customer) c)).collect(Collectors.toList());
      return customerDTOS;
    }

    CustomerDTO toDto(Customer customer){
        return CustomerDTO.builder()
                    .email(customer.getEmail())
                    .ccMail(customer.getCcMail())
                    .address(customer.getAddress())
                    .accountNumber(customer.getAccountNumber())
                    .country(customer.getCountry())
                    .poBox(customer.getPoBox())
                    .nameArabic(customer.getNameArabic())
                    .region(customer.getRegion())
                    .invoiceCurrency(customer.getInvoiceCurrency())
                    .smsaServiceFromSAR(customer.getSmsaServiceFromSAR())
                    .isPresent(customer.isPresent())
                    .nameEnglish(customer.getNameEnglish())
                    .invoiceCurrency(customer.getInvoiceCurrency())
                    .vatNumber(customer.getVatNumber())
                    .status(customer.getStatus())
                    .build();
    }

    Customer toDomain(CustomerDTO customerDTO){
        return Customer.builder()
                .email(customerDTO.getEmail())
                .ccMail(customerDTO.getCcMail())
                .address(customerDTO.getAddress())
                .accountNumber(customerDTO.getAccountNumber())
                .country(customerDTO.getCountry())
                .poBox(customerDTO.getPoBox())
                .nameArabic(customerDTO.getNameArabic())
                .region(customerDTO.getRegion())
                .invoiceCurrency(customerDTO.getInvoiceCurrency())
                .smsaServiceFromSAR(customerDTO.getSmsaServiceFromSAR())
                .isPresent(customerDTO.isPresent())
                .invoiceCurrency(customerDTO.getInvoiceCurrency())
                .nameEnglish(customerDTO.getNameEnglish())
                .VatNumber(customerDTO.getVatNumber())
                .status(customerDTO.getStatus())
                .build();
    }


    public CustomerDTO getCustomerById(String accountNumber) {
        Optional<Customer> customer = customerRepository.findByAccountNumber(accountNumber);
        if (customer.isPresent()){
            return toDto(customer.get());
        }

        throw new RecordNotFoundException(String.format("Customer Not Found On this Id => %d",accountNumber));
    }

    public CustomerDTO updateCustomerByAccountNumber(String accountNumber, CustomerDTO customerDTO) {
        Optional<Customer> customer = customerRepository.findByAccountNumber(accountNumber);
        if(customer.isPresent()){
            customer.get().setAccountNumber(accountNumber);
            customer.get().setCcMail(customerDTO.getCcMail());
            customer.get().setNameEnglish(customerDTO.getNameEnglish());
            customer.get().setNameArabic(customerDTO.getNameArabic());
            customer.get().setEmail(customerDTO.getEmail());
            customer.get().setPresent(customerDTO.isPresent());
            customer.get().setAddress(customerDTO.getAddress());
            customer.get().setVatNumber(customerDTO.getVatNumber());
            customer.get().setSmsaServiceFromSAR(customerDTO.getSmsaServiceFromSAR());
            customer.get().setInvoiceCurrency(customerDTO.getInvoiceCurrency());
            customer.get().setRegion(customerDTO.getRegion());
            customer.get().setPoBox(customerDTO.getPoBox());
            customer.get().setStatus(customerDTO.getStatus());
            customer.get().setCountry(customerDTO.getCountry());
            return toDto(this.customerRepository.save(customer.get()));
        }
        throw new RecordNotFoundException(String.format("Customer Not Found On this Id => %d",accountNumber));
    }

    public CustomerDTO deleteCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()){
            customer.get().setPresent(false);
            return toDto(customerRepository.save(customer.get()));
        }
        throw new RecordNotFoundException(String.format("Customer Not Found On this Id => %d",id));

    }
//    public Customer toDomain(CustomerDTO customerDTO){
//        return modelMapper.map(customerDTO,Customer.class);
//    }
//    public  CustomerDTO toDto(Customer customer){
//        return modelMapper.map(customer,CustomerDTO.class);
//    }

}
