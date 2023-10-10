package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.Exception.RecordNotSaveException;
import com.smsa.backend.model.SalesReportAwb;
import com.smsa.backend.repository.SalesReportAwbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalesReportAwbService {

    @Autowired
    SalesReportAwbRepository salesReportAwbRepository;

    public List<SalesReportAwb> save(List<SalesReportAwb> salesReportAwbs){
        try {
            return this.salesReportAwbRepository.saveAll(salesReportAwbs);
        }catch(Exception ex){
            throw new RecordNotSaveException("Couldn't save data in SalesReportAwb");
        }
    }

    public List<SalesReportAwb> getAllSalesReportAwb(){
        Optional<List<SalesReportAwb>> salesReportAwbs = Optional.of(this.salesReportAwbRepository.findAll());
        if(salesReportAwbs.isPresent()){
            return salesReportAwbs.get();
        }
        throw new RecordNotFoundException("Couldn't find data");
    }



}
