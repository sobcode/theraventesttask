package com.app.theraventesttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This class represents a Data Transfer Object (DTO) used
 * to structure the response for paginated users search results.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedCustomersResponseDTO {
    private List<CustomerResponseDTO> customerList;
    private long numberOfItems;
    private int numberOfPages;
}
