package com.smsa.backend.service;

import com.smsa.backend.dto.CurrencyAuditLogDto;
import com.smsa.backend.dto.RegionDto;
import com.smsa.backend.model.CurrencyAuditLog;
import com.smsa.backend.model.Region;
import com.smsa.backend.repository.CurrencyAuditLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyAuditLogService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CurrencyAuditLogRepository currencyAuditLogRepository;
    public Page<CurrencyAuditLog> getCurrencyAuditLogById(Long currencyId, Pageable pageable) {
        Page<CurrencyAuditLog> currencyAuditLogs = this.currencyAuditLogRepository.findCurrencyAuditByCurrencyId(currencyId, pageable);
        currencyAuditLogs.getContent().forEach(currencyAuditLog -> currencyAuditLog.setCurrency(null));
        return currencyAuditLogs;

    }

    public List<CurrencyAuditLog> getCurrencyAuditLogById(Long currencyId) {
        List<CurrencyAuditLog> currencyAuditLogs = this.currencyAuditLogRepository.findCurrencyAuditByCurrencyId(currencyId);
        currencyAuditLogs.forEach(currencyAuditLog -> currencyAuditLog.setCurrency(null));
        return currencyAuditLogs;

    }



    public CurrencyAuditLog save(CurrencyAuditLog currencyAuditLog){
       return this.currencyAuditLogRepository.save(currencyAuditLog);
    }

    public CurrencyAuditLog toDomain(CurrencyAuditLogDto currencyAuditLogDto){
        return modelMapper.map(currencyAuditLogDto,CurrencyAuditLog.class);
    }
    public CurrencyAuditLogDto toDTo(CurrencyAuditLog currencyAuditLog){
        return modelMapper.map(currencyAuditLog,CurrencyAuditLogDto.class);
    }
}
