package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordAlreadyExistException;
import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.dto.CustomerDTO;
import com.smsa.backend.model.Customer;
import org.modelmapper.ModelMapper;
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

    ModelMapper modelMapper;
    CustomerRepository customerRepository;

    public List<CustomerDTO> getAllCustomer() {
        List<Customer> customer = this.customerRepository.findAllByIsPresent(true);
        if(!customer.isEmpty()){
          return   customer.stream().map(c->toDto(c)).collect(Collectors.toList());
        }
        return new ArrayList<>();

    }

    public CustomerDTO addCustomer(CustomerDTO customerDTO) {
        Optional<Customer> dublicateCustomer = customerRepository.findById(customerDTO.getId());
        if (dublicateCustomer.get()!=null){
            if (!dublicateCustomer.isPresent()){
                dublicateCustomer.get().setPresent(true);
                return toDto(customerRepository.save(dublicateCustomer.get()));
            }
            throw new RecordAlreadyExistException(String.format("Customer   Record Already Exist =>%s",customerDTO));
        }
        customerDTO.setPresent(true);
        return toDto(customerRepository.save(toDomain(customerDTO)));

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
    public Customer toDomain(CustomerDTO customerDTO){
        return modelMapper.map(customerDTO,Customer.class);
    }
    public  CustomerDTO toDto(Customer customer){
        return modelMapper.map(customer,CustomerDTO.class);
    }

}
