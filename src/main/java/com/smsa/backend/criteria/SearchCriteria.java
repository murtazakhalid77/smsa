package com.smsa.backend.criteria;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SearchCriteria {
    String mapper;
    String searchText;
}
