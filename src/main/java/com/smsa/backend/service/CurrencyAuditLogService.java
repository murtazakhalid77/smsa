package com.smsa.backend.service;

import com.smsa.backend.dto.CurrencyAuditLogDto;
import com.smsa.backend.dto.RegionDto;
import com.smsa.backend.model.CurrencyAuditLog;
import com.smsa.backend.model.Region;
import com.smsa.backend.repository.CurrencyAuditLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyAuditLogService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CurrencyAuditLogRepository currencyAuditLogRepository;
    public List<CurrencyAuditLogDto> getCurrencyAuditLogById(Long currencyId) {
        List<CurrencyAuditLogDto> currencyAuditLogDtos = new ArrayList<>();
        List<CurrencyAuditLog> currencyAuditLogs = this.currencyAuditLogRepository.findCurrencyAuditByCurrencyId(currencyId);
        for(CurrencyAuditLog currencyAuditLog1: currencyAuditLogs){
            currencyAuditLog1.setCurrency(null);
            currencyAuditLogDtos.add(toDTo(currencyAuditLog1));
        }
        return currencyAuditLogDtos;
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
