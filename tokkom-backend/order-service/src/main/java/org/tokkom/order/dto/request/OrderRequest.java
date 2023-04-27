package org.tokkom.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private String productId;
    private String productTitle;
    private Double productQty;
    private String customerName;
    private String customerAddress;
    private String customerPhoneNumber;
    private String description;
}
