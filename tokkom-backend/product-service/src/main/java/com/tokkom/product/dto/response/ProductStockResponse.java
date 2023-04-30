package com.tokkom.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStockResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean isProductInStock;
}
