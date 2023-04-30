package com.tokkom.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private Double price;
    private Double stock;
    private String brand;
    private String category;

}
