package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.RegionDto;
import com.smsa.backend.dto.SalesReportDto;
import com.smsa.backend.dto.SearchSalesReportDto;
import com.smsa.backend.model.Region;
import com.smsa.backend.model.SalesReport;
import com.smsa.backend.model.SalesReportAwb;
import com.smsa.backend.repository.SalesReportRepository;
import com.smsa.backend.specification.FilterSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

    public Page<SalesReport> getSalesReport(SearchSalesReportDto searchSalesReportDto, Pageable pageable) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMapper(searchSalesReportDto.getMapper());
        searchCriteria.setSearchText(searchSalesReportDto.getSearch());

        Optional<Page<SalesReport>> salesReports = null;
        List<SalesReportDto> salesReportDtos = new ArrayList<>();

        if(searchCriteria.getSearchText() == null){
            if(searchSalesReportDto.getInvoiceTo()!=null || searchSalesReportDto.getInvoiceFrom()!=null){
                if(searchSalesReportDto.getInvoiceTo().length()<5 || searchSalesReportDto.getInvoiceFrom().length()<5){
                    throw new RecordNotFoundException(String.format("Invalid format"));
                }else{
                    salesReports = Optional.of(this.salesReportRepository.findByInvoiceNumberBetween(searchSalesReportDto.getInvoiceTo().substring(5),
                            searchSalesReportDto.getInvoiceFrom().substring(5), pageable));
                }
            }else if(searchSalesReportDto.getAwbs()!=null){
                salesReports = this.getSalesReportByAwbs(searchSalesReportDto.getAwbs(), pageable);
            }
            else{
                salesReports = Optional.of(this.salesReportRepository.findAllByCreatedAtBetween(
                        this.helperService.convertStringInToLocalDate(searchSalesReportDto.getStartDate()),
                        this.helperService.convertStringInToLocalDate(searchSalesReportDto.getEndDate()), pageable));
            }
        }else{
            Specification<SalesReport> regionSpecification = salesReportFilterSpecification.getSearchSpecification(searchCriteria);
            salesReports = Optional.of(this.salesReportRepository.findAll(regionSpecification, pageable));
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

    public Optional<Page<SalesReport>> getSalesReportByAwbs(String awbs, Pageable pageable){
        List<String> awbList = Arrays.stream(awbs.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        Optional<Page<SalesReport>> salesReport = Optional.of(this.salesReportRepository.getSalesReportByAwbs(awbList, pageable));
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
