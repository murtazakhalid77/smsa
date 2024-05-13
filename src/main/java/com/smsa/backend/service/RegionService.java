package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordAlreadyExistException;
import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.RegionDto;
import com.smsa.backend.model.Region;
import com.smsa.backend.repository.RegionRepository;
import com.smsa.backend.specification.FilterSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionService {

    @Autowired
    RegionRepository regionRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    FilterSpecification<Region> regionFilterSpecification;

    public RegionDto addRegion(RegionDto regionDto) {
        Optional<Region> region = this.regionRepository.findByCustomerRegion(regionDto.getCustomerRegion());
        if(!region.isPresent()){
            return toDTo(this.regionRepository.save(toDomain(regionDto)));
        }
        throw new RecordAlreadyExistException(String.format("Region already exist"));
    }

    public Page<Region> getAllRegions(SearchCriteria searchCriteria, Pageable pageable) {
        Optional<Page<Region>> regions;
        if(searchCriteria.getSearchText() == null){
            regions = Optional.of(this.regionRepository.findAll(pageable));
        }else{
            Specification<Region> regionSpecification = regionFilterSpecification.getSearchSpecification(searchCriteria);
            regions = Optional.of(this.regionRepository.findAll(regionSpecification, pageable));
        }

        return regions.isPresent() ? regions.get() : null;
    }

    public List<Region> getAllRegions() {
        return this.regionRepository.findAll();
    }


    public RegionDto getRegionById(Long id) {
        Optional<Region> region = this.regionRepository.findById(id);
        if(region.isPresent()){
            return toDTo(region.get());
        }
        throw new RecordNotFoundException(String.format("Region not found"));
    }

    public RegionDto updateRegion(Long id, RegionDto regionDto) {
        Optional<Region> region = this.regionRepository.findById(id);
        if(region.isPresent()){
            region.get().setCustomerRegion(regionDto.getCustomerRegion());
            region.get().setVat(regionDto.getVat());
            region.get().setHeaderName(regionDto.getHeaderName());
            region.get().setVatNumber(regionDto.getVatNumber());
            region.get().setDescription(regionDto.getDescription());
            region.get().setStatus(regionDto.isStatus());
            return toDTo(this.regionRepository.save(region.get()));
        }
        throw new RecordNotFoundException(String.format("Region not found"));
    }

    public List<RegionDto> toDto(List<Region> regions){
        return regions.stream().map(region -> toDTo(region)).collect(Collectors.toList());
    }

    public Region toDomain(RegionDto regionDto){
        return modelMapper.map(regionDto,Region.class);
    }
    public RegionDto toDTo(Region region){
        return modelMapper.map(region,RegionDto.class);
    }

    public List<RegionDto> getAllRegionsByStatus(boolean status) {
        List<Region> regions = this.regionRepository.findByStatus(status);
        if(regions != null && regions.size() > 0){
            List<RegionDto> regionDtos = regions.stream()
                    .filter(region -> region.getStatus())
                    .map(region -> toDTo(region))
                    .collect(Collectors.toList());
            return regionDtos;
        }
        throw new RecordNotFoundException(String.format("Regions not found"));
    }

    public List<Region> getRegions() {
        Optional<List<Region>> regions = Optional.of(this.regionRepository.findAll());
        return regions.isPresent() ? regions.get() : null;
    }
}
