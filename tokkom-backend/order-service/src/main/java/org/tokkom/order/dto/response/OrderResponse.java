package org.tokkom.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String productId;
    private String productTitle;
    private Double productQty;
    private String customerName;
    private String customerAddress;
    private String customerPhoneNumber;
    private String description;
}
