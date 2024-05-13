package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.SalesReportDto;
import com.smsa.backend.dto.SearchSalesReportDto;
import com.smsa.backend.model.SalesReport;
import com.smsa.backend.repository.SalesReportRepository;
import com.smsa.backend.specification.FilterSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<SalesReport> getSalesReport(SearchSalesReportDto searchSalesReportDto) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMapper(searchSalesReportDto.getMapper());
        searchCriteria.setSearchText(searchSalesReportDto.getSearch());

        Optional<List<SalesReport>> salesReports = null;
        List<SalesReportDto> salesReportDtos = new ArrayList<>();

        if(searchCriteria.getSearchText().length() == 0){
            if(searchSalesReportDto.getInvoiceTo()!=null || searchSalesReportDto.getInvoiceFrom()!=null){
                if(searchSalesReportDto.getInvoiceTo().length()<5 || searchSalesReportDto.getInvoiceFrom().length()<5){
                    throw new RecordNotFoundException(String.format("Invalid format"));
                }else{
                    salesReports = Optional.of(this.salesReportRepository.findByInvoiceNumberBetween(searchSalesReportDto.getInvoiceTo().substring(5),
                            searchSalesReportDto.getInvoiceFrom().substring(5)));
                }
            }else if(searchSalesReportDto.getAwbs()!=null){
                salesReports = this.getSalesReportByAwbs(searchSalesReportDto.getAwbs());
            }
            else{
                salesReports = Optional.of(this.salesReportRepository.findAllByCreatedAtBetween(
                        this.helperService.convertStringInToLocalDate(searchSalesReportDto.getStartDate()),
                        this.helperService.convertStringInToLocalDate(searchSalesReportDto.getEndDate())));
            }
        }else{
            Specification<SalesReport> regionSpecification = salesReportFilterSpecification.getSearchSpecification(searchCriteria);
            salesReports = Optional.of(this.salesReportRepository.findAll(regionSpecification));
        }


        for (SalesReport salesReport : salesReports.get()) {
            salesReport.setTotalChargesAsPerCustomerDeclarationForm(decimalFormat(String.valueOf(salesReport.getTotalChargesAsPerCustomerDeclarationForm())));
            salesReport.setSmsaFeeCharges(decimalFormat(String.valueOf(salesReport.getSmsaFeeCharges())));
            salesReport.setVatOnSmsaFees(decimalFormat(String.valueOf(salesReport.getVatOnSmsaFees())));
            salesReport.setTotalAmount(decimalFormat(String.valueOf(salesReport.getTotalAmount())));
        }

        return salesReports.get();
    }

    public Double decimalFormat(String value){
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedValue = value.replace(",", "").trim();
        double convertInToDouble = Double.parseDouble(formattedValue);

        // Format the double value with exactly two decimal places
        String requiredValue = decimalFormat.format(convertInToDouble);

        // Parse the formatted string back to double
        return Double.parseDouble(requiredValue);
    }

    public Optional<List<SalesReport>> getSalesReportByAwbs(String awbs){
        List<String> awbList = Arrays.stream(awbs.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        Optional<List<SalesReport>> salesReport = Optional.of(this.salesReportRepository.getSalesReportByAwbs(awbList));
        if(salesReport.isPresent()){
            return salesReport;
        }
        throw new RecordNotFoundException("Couldn't find data");
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
