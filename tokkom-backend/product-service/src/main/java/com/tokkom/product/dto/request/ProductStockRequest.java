package com.tokkom.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStockRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double stock;
}
