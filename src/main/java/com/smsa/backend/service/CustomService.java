package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.dto.CustomDto;
import com.smsa.backend.model.Custom;
import com.smsa.backend.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomService {

    @Autowired
    CustomRepository customRepository;
    public CustomDto addCustom(CustomDto customDto) {
        return toDto(this.customRepository.save(toDomain(customDto)));
    }

    public List<CustomDto> getAllCustoms() {
        List<CustomDto> customDtos = new ArrayList<>();

        List<Custom> customs = this.customRepository.findAll();

        for(Custom custom: customs){
            customDtos.add(toDto(custom));
        }

        return customDtos;
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
            custom.get().setPresent(customDto.isPresent());

            return toDto(this.customRepository.save(custom.get()));
        }
        throw new RecordNotFoundException(String.format("Custom Not Found On this Id => %d",id));
    }


    private CustomDto toDto(Custom custom){
        return new CustomDto().builder()
                .id(custom.getId())
                .custom(custom.getCustom())
                .customPort(custom.getCustomPort())
                .smsaFeeVat(custom.getSmsaFeeVat())
                .isPresent(custom.isPresent())
                .build();
    }
    private Custom toDomain(CustomDto customDto){
        return new Custom().builder()
                .id(customDto.getId())
                .custom(customDto.getCustom())
                .customPort(customDto.getCustomPort())
                .smsaFeeVat(customDto.getSmsaFeeVat())
                .isPresent(customDto.isPresent())
                .build();
    }

}
