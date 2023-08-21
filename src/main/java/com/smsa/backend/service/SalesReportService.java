package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.RegionDto;
import com.smsa.backend.dto.SalesReportDto;
import com.smsa.backend.dto.SearchSalesReportDto;
import com.smsa.backend.model.Region;
import com.smsa.backend.model.SalesReport;
import com.smsa.backend.repository.SalesReportRepository;
import com.smsa.backend.specification.FilterSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
@Service
public class SalesReportService {

    @Autowired
    SalesReportRepository salesReportRepository;
    @Autowired
    FilterSpecification<SalesReport> salesReportFilterSpecification;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    HelperService helperService;

    public List<SalesReportDto> getSalesReport(SearchSalesReportDto searchSalesReportDto) {
        List<SalesReport> salesReports = null;
        List<SalesReportDto> salesReportDtos = new ArrayList<>();

        if(searchSalesReportDto.getInvoiceTo()!=null || searchSalesReportDto.getInvoiceFrom()!=null){
            if(searchSalesReportDto.getInvoiceTo().length()<5 || searchSalesReportDto.getInvoiceFrom().length()<5){
                throw new RecordNotFoundException(String.format("Invalid format"));
            }
        }

        if(searchSalesReportDto.getInvoiceTo()!=null || searchSalesReportDto.getInvoiceFrom()!=null){
            salesReports = this.salesReportRepository.findByInvoiceNumberBetween(searchSalesReportDto.getInvoiceTo().substring(5),
                    searchSalesReportDto.getInvoiceFrom().substring(5));
        }
        else{
            salesReports = this.salesReportRepository.findAllByCreatedAtBetween(
                    this.helperService.convertStringInToLocalDate(searchSalesReportDto.getStartDate()),
                    this.helperService.convertStringInToLocalDate(searchSalesReportDto.getEndDate()));
        }
        for(SalesReport salesReport: salesReports){
            salesReport.setTotalChargesAsPerCustomerDeclarationForm(Double.valueOf(formatCurrency(salesReport.getTotalChargesAsPerCustomerDeclarationForm())));
            salesReportDtos.add(this.toDTo(salesReport));
        }
        return salesReportDtos;
    }

    public SalesReport toDomain(SalesReportDto salesReportDto){
        return modelMapper.map(salesReportDto,SalesReport.class);
    }
    public SalesReportDto toDTo(SalesReport salesReport){
        return modelMapper.map(salesReport,SalesReportDto.class);
    }

    private String formatCurrency(Double value) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(value);
    }
}
