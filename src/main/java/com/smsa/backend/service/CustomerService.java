package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordAlreadyExistException;
import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.dto.CustomerDTO;
import com.smsa.backend.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import com.smsa.backend.repository.CustomerRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

//    ModelMapper modelMapper;
    @Autowired
    CustomerRepository customerRepository;

    public List<CustomerDTO> getAllCustomer() {
        List<Customer> customer = this.customerRepository.findAllByIsPresent(true);
        if(!customer.isEmpty()){
          return   customer.stream().map(c->toDto(c)).collect(Collectors.toList());
        }
        return new ArrayList<>();

    }

    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
            if (customerDTO.getId()!=null){
                Optional<Customer> duplicateCustomer = customerRepository.findById(customerDTO.getId());
                if(duplicateCustomer.isPresent()){
                    return toDto(customerRepository.save(toDomain(customerDTO)));
                }else{
                    throw new RecordAlreadyExistException(String.format("Customer Record Already Exist =>%s",customerDTO));
                }
            }else{
                Customer customer = toDomain(customerDTO);
                System.out.println(customer);
                return toDto(customerRepository.save(toDomain(customerDTO)));
            }

    }

    CustomerDTO toDto(Customer customer){
        return new CustomerDTO().builder()
                    .id(customer.getId())
                    .email(customer.getEmail())
                    .address(customer.getAddress())
                    .accountNumber(customer.getAccountNumber())
                    .country(customer.getCountry())
                    .poBox(customer.getPoBox())
                    .customerNameArabic(customer.getCustomerNameArabic())
                    .currencyRateFromSAR(customer.getCurrencyRateFromSAR())
                    .invoiceCurrency(customer.getInvoiceCurrency())
                    .smsaServiceFromSAR(customer.getSmsaServiceFromSAR())
                    .isPresent(customer.isPresent())
                    .nameEnglish(customer.getNameEnglish())
                    .invoiceCurrency(customer.getInvoiceCurrency())
                    .vatNumber(customer.getVatNumber())
                    .build();
    }

    Customer toDomain(CustomerDTO customerDTO){
        return new Customer().builder()
                .Id(customerDTO.getId())
                .email(customerDTO.getEmail())
                .address(customerDTO.getAddress())
                .accountNumber(customerDTO.getAccountNumber())
                .country(customerDTO.getCountry())
                .poBox(customerDTO.getPoBox())
                .customerNameArabic(customerDTO.getCustomerNameArabic())
                .currencyRateFromSAR(customerDTO.getCurrencyRateFromSAR())
                .invoiceCurrency(customerDTO.getInvoiceCurrency())
                .smsaServiceFromSAR(customerDTO.getSmsaServiceFromSAR())
                .isPresent(customerDTO.isPresent())
                .invoiceCurrency(customerDTO.getInvoiceCurrency())
                .NameEnglish(customerDTO.getNameEnglish())
                .VatNumber(customerDTO.getVatNumber())
                .build();
    }


    public CustomerDTO getCustomerById(Long id) {
        Optional<Customer> coupon = customerRepository.findById(id);
        if (coupon.isPresent()){
            return toDto(coupon.get());
        }

        throw new RecordNotFoundException(String.format("Customer Not Found On this Id => %d",id));
    }

    public CustomerDTO updateCustomerById(CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customerDTO.getId()).get();
        customer.setNameEnglish(customerDTO.getNameEnglish());
        customer.setCustomerNameArabic(customerDTO.getCustomerNameArabic());
        customer.setEmail(customerDTO.getEmail());
        customer.setPresent(customerDTO.isPresent());
        customer.setAddress(customerDTO.getAddress());
        customer.setVatNumber(customerDTO.getAccountNumber());
        customer.setSmsaServiceFromSAR(customerDTO.getSmsaServiceFromSAR());
        customer.setAccountNumber(customerDTO.getAccountNumber());
        customer.setInvoiceCurrency(customerDTO.getInvoiceCurrency());
        customer.setCurrencyRateFromSAR(customerDTO.getCurrencyRateFromSAR());
        customer.setPoBox(customerDTO.getPoBox());

        customerRepository.save(customer);
        return toDto(customerRepository.save(customer));
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
