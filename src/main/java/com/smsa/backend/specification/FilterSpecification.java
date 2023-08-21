package com.smsa.backend.specification;

import com.smsa.backend.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public class FilterSpecification<T>{
    public Specification<T> getSearchSpecification(SearchCriteria searchCriteria) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(root.get(searchCriteria.getColumn()), searchCriteria.getStartDate(), searchCriteria.getColumn());
            }
        };
    }
}
