package com.tokkom.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String title;
    private String description;
    private Double price;
    private Double stock;
    private String brand;
    private String category;
    private String thumbnail;
    private List<String> images;
}
