package com.tokkom.product.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String title;
    private String description;
    private Double price;
    private Double stock;
    private String brand;
    private String category;
    private String thumbnail;
    private List<String> images;
    private String url;
}
