package com.iyzico.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {
    private Integer numberOfElements;
    private Long totalNumberOfElements;
    private Integer totalPages;
    private List<T> data;
}