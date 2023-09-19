package com.smsa.backend.specification;

import com.smsa.backend.Mapper;
import com.smsa.backend.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class FilterSpecification<T>{
    public Specification<T> getSearchSpecification(SearchCriteria searchCriteria) {

        searchCriteria.setSearchText(searchCriteria.getSearchText()
                .replace("\\", "\\\\")
                .replace("%2B","\\+")
                .replace("%", "\\%")
                .replace("_", "\\_")
                .trim());

        if (searchCriteria.getMapper().equalsIgnoreCase(Mapper.USER.toString())) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchCriteria.getSearchText().toLowerCase() + "%");
        } else if (searchCriteria.getMapper().equalsIgnoreCase(Mapper.CUSTOMER.toString())) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("accountNumber")), "%" + searchCriteria.getSearchText().toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nameEnglish")), "%" + searchCriteria.getSearchText().toLowerCase() + "%")
            );
        } else if (searchCriteria.getMapper().equalsIgnoreCase(Mapper.CURRENCY.toString())) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("currencyFrom")), "%" + searchCriteria.getSearchText().toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("currencyTo")), "%" + searchCriteria.getSearchText().toLowerCase() + "%")
            );
        } else if (searchCriteria.getMapper().equalsIgnoreCase(Mapper.CUSTOM_PORT.toString())) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("custom")), "%" + searchCriteria.getSearchText().toLowerCase() + "%")
            );
        } else if (searchCriteria.getMapper().equalsIgnoreCase(Mapper.REGION.toString())) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("customerRegion")), "%" + searchCriteria.getSearchText().toLowerCase() + "%")
            );
        }
        return null;
    }

}
