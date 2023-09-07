package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.dto.CustomDto;
import com.smsa.backend.model.Custom;
import com.smsa.backend.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomService {

    @Autowired
    CustomRepository customRepository;
    public CustomDto addCustom(CustomDto customDto) {
        return toDto(this.customRepository.save(toDomain(customDto)));
    }

    public Page<Custom> getAllCustoms(Pageable pageable) {
        List<CustomDto> customDtos = new ArrayList<>();

        Page<Custom> customs = this.customRepository.findAll(pageable);

        if(!customs.isEmpty()){
            return customs;
        }
        return null;
    }

    public List<CustomDto> getAllCustomForImport() {
        try {
            List<CustomDto> customDtos = this.customRepository.findByIsPresent();
            return customDtos;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<String> getDistinctCurrencies(){
        try {
            return this.customRepository.getDistinctCurrencies();
        }catch (Exception e){
            e.printStackTrace();
            throw new RecordNotFoundException(String.format("Currency Not Found"));
        }
    }

    public CustomDto getCustomById(Long id) {
        Optional<Custom> custom = this.customRepository.findById(id);
        if(custom.isPresent()){
            return toDto(custom.get());
        }
        throw new RecordNotFoundException(String.format("Custom Not Found On this Id => %d",id));
    }



    public CustomDto updateCustom(CustomDto customDto, Long id) {
        Optional<Custom> custom = this.customRepository.findById(id);

        if(custom.isPresent()){
            custom.get().setId(id);
            custom.get().setCustomPort(customDto.getCustomPort());
            custom.get().setCustom(customDto.getCustom());
            custom.get().setSmsaFeeVat(customDto.getSmsaFeeVat());
            custom.get().setCurrency(customDto.getCurrency());
            custom.get().setPresent(customDto.isPresent());

            return toDto(this.customRepository.save(custom.get()));
        }
        throw new RecordNotFoundException(String.format("Custom Not Found On this Id => %d",id));
    }


    public List<CustomDto> toDto(List<Custom> customs){
        return customs.stream().map(custom -> toDto(custom)).collect(Collectors.toList());
    }

    private CustomDto toDto(Custom custom){
        return new CustomDto().builder()
                .id(custom.getId())
                .custom(custom.getCustom())
                .customPort(custom.getCustomPort())
                .smsaFeeVat(custom.getSmsaFeeVat())
                .currency(custom.getCurrency())
                .isPresent(custom.isPresent())
                .build();
    }
    private Custom toDomain(CustomDto customDto){
        return new Custom().builder()
                .id(customDto.getId())
                .custom(customDto.getCustom())
                .customPort(customDto.getCustomPort())
                .smsaFeeVat(customDto.getSmsaFeeVat())
                .currency(customDto.getCurrency())
                .isPresent(customDto.isPresent())
                .build();
    }

}
